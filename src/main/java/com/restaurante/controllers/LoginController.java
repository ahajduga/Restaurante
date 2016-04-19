package com.restaurante.controllers;

import com.restaurante.persistence.dao.UserDao;
import com.restaurante.utils.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by alex on 29.03.16.
 */
@PreAuthorize("permitAll")
@RestController
public class LoginController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public LoginHandler isAuthenticated(){
        RequestContextHolder.currentRequestAttributes().getSessionId();
        if(("" + SecurityContextHolder.getContext().getAuthentication().getAuthorities()).equals("[ROLE_ANONYMOUS]")){
            return new LoginHandler(RequestContextHolder.currentRequestAttributes().getSessionId(),
                    "ERR",
                    null,
                    "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            if(username!=null && !username.equals("")){
                username = username.split("Username: ")[1];
                username = username.split(";")[0];
            }
            return new LoginHandler(RequestContextHolder.currentRequestAttributes().getSessionId(),
                    "OK",
                    userDao.findByUsername(username),
                    "" + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        }
    }
}