package com.restaurante.persistence;


import javax.persistence.*;

/**
 * Created by alex on 29.03.16.
 */
@Entity
@javax.persistence.Table(name = "sys_users")
public class User {
@Id
private Long id;
    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    private String login;
    private int type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
