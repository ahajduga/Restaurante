package com.restaurante.persistence;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alex on 29.03.16.
 */
@Entity
@javax.persistence.Table(name="booking")
public class Booking {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Column(name="mail")
    private String mail;


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
