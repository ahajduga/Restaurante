package com.restaurante.utils;

import com.restaurante.persistence.User;

/**
 * Created by alex on 09.05.15.
 */
public class LoginHandler {

    private final String jsessionid;
    private final String state;
    private final User user;
    private final String msg;

    public LoginHandler(String id, String state, User user, String msg) {
        this.jsessionid = id;
        this.state = state;
        this.user = user;
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public User getUser() {
        return user;
    }
}
