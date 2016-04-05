package com.restaurante.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by alex on 29.03.16.
 */
@Entity
public class User {
@Id
    private String login;
    private int type;
}
