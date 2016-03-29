package com.restaurante.controllers;

import com.restaurante.persistence.Reservation;
import com.restaurante.persistence.Table;

import java.util.Date;
import java.util.List;

/**
 * Created by alex on 29.03.16.
 */
public class ReservationController {

    public List<Reservation> getReservation(Date date, Table table, boolean active){
        return null;
    }

    public boolean makeReservation(Date date, Table table, String customer){
        return true;
    }

    public boolean updateReservation(Reservation reservation){
        return true;
    }

    public boolean deleteReservation(Reservation reservation){
        return true;
    }
}
