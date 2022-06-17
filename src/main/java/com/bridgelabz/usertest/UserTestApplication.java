package com.bridgelabz.usertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserTestApplication.class, args);
        System.out.println("Welcome to the user app");
    }

}
