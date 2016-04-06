package com.restaurante.controllers;

import com.restaurante.persistence.Booking;
import com.restaurante.persistence.Table;
import com.restaurante.persistence.dao.BookingDao;
import com.restaurante.persistence.dao.TableDao;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 29.03.16.
 */
@RestController
public class BookingController {

    @Autowired
    TableDao tableDao;

    @Autowired
    BookingDao bookingDao;
@RequestMapping(value = "/book/{id}", method= RequestMethod.GET)
    public Map<String,String> bookTable(
            @PathVariable Integer tableID,
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
                resultMap.put("msg","ok");
            }
        }
        }
        return null;
    }


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
