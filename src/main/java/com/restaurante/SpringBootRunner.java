package com.restaurante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan
public class SpringBootRunner {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootRunner.class, args);
    }
}