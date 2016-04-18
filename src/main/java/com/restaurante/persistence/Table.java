package com.restaurante.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by alex on 29.03.16.
 */
@Entity
public class Table {
    @Id
    @Column(name="table_ID")
    private int tableID;

    @Column(name="seats")
    private int seats;

    @Column(name="bookings")
    @OneToMany
    private List<Booking> bookings;

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
