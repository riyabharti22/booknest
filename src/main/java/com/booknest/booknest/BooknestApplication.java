package com.booknest.booknest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BooknestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooknestApplication.class, args);
    }
}