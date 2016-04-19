package com.restaurante.controllers;

import com.restaurante.persistence.UserRole;
import com.restaurante.persistence.User;
import com.restaurante.persistence.dao.UserDao;
import com.restaurante.persistence.dao.UserRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by alex on 29.03.16.
 */
@PreAuthorize("permitAll")
@RestController
public class AccountController {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User createUser(
            @RequestParam(value = "login", required = true) String login,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "mail", required = true) String mail,
            @RequestParam(value = "type", required = true) Integer type
    ) {
        if (userDao.findByUsername(login) != null) {
            //username already exists
            return null;
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(new ShaPasswordEncoder().encodePassword(password,""));
        user.setMail(mail);
        userDao.save(user);
        switch (type){
            case 1:
                userRoleDao.save(new UserRole("ROLE_ADMIN", user));
                break;
            case 2:
                userRoleDao.save(new UserRole("ROLE_EMPLOYEE", user));
                break;
            case 3:
                userRoleDao.save(new UserRole("ROLE_CUSTOMER", user));
                break;
            default:
                userRoleDao.save(new UserRole("ROLE_CUSTOMER", user));
        }
        return user;
    }

    @RequestMapping(value = "/users/{login}", method = RequestMethod.POST)
    public User updateUserData(
            @PathVariable String login,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "type", required = false) Integer type
    ) {
        User user = null;
        for (User p : userDao.findAll()) {
            if (p.getLogin().equals(login)) {
                user = p;
                break;
            }
        }
        if (password != null)
            user.setPassword(password);
        if (mail != null)
            user.setLogin(mail);
//        if (type != null)
//            user.setType(type);
        userDao.save(user);

        return user;
    }

    @RequestMapping(value = "/users/{login}", method = RequestMethod.DELETE)
    public boolean deleteUser(@PathVariable String login) {
        User user = null;
        for (User p : userDao.findAll()) {
            if (p.getLogin().equals(login)) {
                user = p;
                break;
            }
        }
        for (UserRole u : user.getRoles()) {
            userRoleDao.delete(u);
        }
        userDao.delete(user);
        return true;
    }

    @RequestMapping(value = "/users/{login}", method = RequestMethod.GET)
    public User getUserData(@PathVariable String login) {
        User user = null;
        for (User p : userDao.findAll()) {
            if (p.getLogin().equals(login)) {
                user = p;
                break;
            }
        }
        return user;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Iterable<User> getAllUsersData() {
        return userDao.findAll();
    }
}
