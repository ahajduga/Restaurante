package com.restaurante.persistence;


import javax.persistence.*;

/**
 * Created by alex on 29.03.16.
 */
@Entity
@javax.persistence.Table(name = "sys_users")
public class User {
@Id
    private String login;
    private int type;
}
