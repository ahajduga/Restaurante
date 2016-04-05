package com.restaurante.persistence;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by alex on 29.03.16.
 */
public class Booking {

    @Column(name="booking_start")
    private Date dateStart;

    @Column(name="booking_end")
    private Date dateEnd;

    @Column(name="user_id")
    private Integer customer;

    @Column(name="email")
    private String email;

    @Column(name="active")
    private boolean active;

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
