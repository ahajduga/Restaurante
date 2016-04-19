package com.restaurante.controllers;

import com.restaurante.persistence.Booking;
import com.restaurante.persistence.Table;
import com.restaurante.persistence.User;
import com.restaurante.persistence.dao.BookingDao;
import com.restaurante.persistence.dao.TableDao;
import com.restaurante.persistence.dao.UserDao;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.mail.MailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by alex on 29.03.16.
 */
@PreAuthorize("permitAll")
@RestController
public class BookingController {

    @Autowired
    TableDao tableDao;

    @Autowired
    BookingDao bookingDao;

    @Autowired
    UserDao userDao;
    @Autowired
    JavaMailSender javaMailSender;
    private void send(String to, String subject, String message) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        javaMailSender.send(mail);
    }




    @RequestMapping(value = "/getActiveReservations", method = RequestMethod.GET)
    public List<Booking> getActiveBookings(){
        return bookingDao.findByActive(true);

    }
    @RequestMapping(value = "/disableReservation", method = RequestMethod.GET)
    public void disableReservation(
            @RequestParam(value = "booking_ID", required = true) Long bookingID){
        Booking booking = bookingDao.findOne(bookingID);
        booking.setActive(false);
        bookingDao.save(booking);
    }

    @RequestMapping(value = "/book/{id}", method= RequestMethod.GET)
    public Map<String,String> bookTable(
            @PathVariable Integer tableID,
            @RequestParam(value="user_ID", required = true) Long userID,
            @RequestParam(value="from", required = true) String dateFrom,
            @RequestParam(value="to", required = true) String dateTo) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.ENGLISH);
        Date from = format.parse(dateFrom);
        Date to = format.parse(dateTo);
        Map<String,String> resultMap = new HashMap<>();
        for(Booking b : tableDao.findOne((long)tableID).getBookings()){
            if(!b.isActive()){
                if((!b.getDateStart().before(from) && !b.getDateStart().after(to)) || !b.getDateEnd().after(to) && !b.getDateEnd().before(from)){
                    resultMap.put("msg", "invalid");
                }
                else{
                    Table table = tableDao.findOne((long)tableID);
                    Booking bookingToSave = new Booking();
                    bookingToSave.setActive(true);
                    bookingToSave.setDateStart(from);
                    bookingToSave.setDateEnd(to);
                    table.getBookings().add(bookingToSave);
                    tableDao.save(table);
                    User user = userDao.findOne(userID);
                    String message = "Rezerwacja zatwierdzona dla uzytkownika " + user.getLogin() + ", na czas od " + from.toString() + " do " + to.toString();
                    send(user.getMail(),"Rezerwacja zatwierdzona", message);
                    resultMap.put("msg","ok");
                }
            }
        }
        return null;
    }


    @RequestMapping(value = "/getLatestReservations", method = RequestMethod.GET)
    public List<Booking> getLatestReservations(
            @RequestParam(value = "from", required = true) String dateFrom) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.ENGLISH);
        Date from = format.parse(dateFrom);
        List<Booking> result = new LinkedList<>();
        Iterator<Booking> iterator = bookingDao.findAll().iterator();
        while(iterator.hasNext()){
            Booking currentBooking = iterator.next();
            if(currentBooking.getDateEnd().after(from))
                result.add(currentBooking);
        }
        return result;
    }




    @RequestMapping(value = "/getFreeTables", method = RequestMethod.GET)
    public TreeSet<Table> getFreeTables(
            @RequestParam(value = "from", required = true) String dateFrom,
            @RequestParam(value = "to", required = true) String dateTo) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.ENGLISH);
        Date from = format.parse(dateFrom);
        Date to = format.parse(dateTo);

        TreeSet<Table> freeTables = new TreeSet<>();

        for(Table t : tableDao.findAll()) {

            boolean available = true;

            for (Booking b : t.getBookings()) {
                if (b.isActive()
                        && (b.getDateStart().after(from) || b.getDateStart().equals(from))
                        && (b.getDateEnd().before(to) || b.getDateEnd().equals(to))) {

                    available = false;
                    continue;
                }
            }

            if(available){
                freeTables.add(t);
            }
        }

        return freeTables;
    }

    @RequestMapping(value = "/getFreeBookings", method = RequestMethod.GET)
    public TreeMap<Date, Date> getFreeBookings(
            @RequestParam(value = "table_ID", required = true) Integer tableID,
            @RequestParam(value = "from", required = true) String dateFrom,
            @RequestParam(value = "to", required = true) String dateTo) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.ENGLISH);
        Date from = format.parse(dateFrom);
        Date to = format.parse(dateTo);

        TreeMap<Date, Date> freeBookings = new TreeMap<>();
        for(Date i=from; i.before(to);){
            Date toTemp = DateUtils.addHours(i, 1);
            freeBookings.put(i, toTemp);
            i = toTemp;
        }

        for(Booking b : tableDao.findOne((long) tableID).getBookings()){
            if(b.isActive()
                    && (b.getDateStart().after(from) || b.getDateStart().equals(from))
                    && (b.getDateEnd().before(to) || b.getDateEnd().equals(to))){

                for(Date i=b.getDateStart(); i.before(b.getDateEnd());){
                    Date toTemp = DateUtils.addHours(i, 1);
                    freeBookings.remove(i, toTemp);
                    i = toTemp;
                }
            }
        }

        return freeBookings;
    }

    @RequestMapping(value = "/getBookings", method = RequestMethod.GET)
    public List<Booking> getBookings(
            @RequestParam(value = "table_ID", required = true) Integer tableID,
            @RequestParam(value = "from", required = true) String dateFrom,
            @RequestParam(value = "to", required = true) String dateTo) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.ENGLISH);
        Date from = format.parse(dateFrom);
        Date to = format.parse(dateTo);

        LinkedList<Booking> bookings = new LinkedList<>();

        for(Booking b : tableDao.findOne((long) tableID).getBookings()){
            if(b.isActive()
                    && (b.getDateStart().after(from) || b.getDateStart().equals(from))
                    && (b.getDateEnd().before(to) || b.getDateEnd().equals(to))){

                bookings.add(b);
            }
        }

        return bookings;
    }

    public boolean makeBooking(Date date, Table table, String customer){
        return true;
    }

    public boolean updateBooking(Booking booking){
        return true;
    }

    public boolean deleteBooking(Booking booking){
        return true;
    }
}
