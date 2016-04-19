package com.restaurante.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alex on 29.03.16.
 */
@PreAuthorize("isAuthenticated()")
@RestController
public class LoginTestController {

    @RequestMapping("/test")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {

        System.out.println("works");

        return name;
    }
}