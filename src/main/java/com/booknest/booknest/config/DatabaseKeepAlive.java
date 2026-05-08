package com.booknest.booknest.config;

import com.booknest.booknest.userbooks.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseKeepAlive {

    private final UserBookRepository userBookRepository;

    @Scheduled(fixedRate = 23 * 60 * 60 * 1000)
    public void keepAlive() {
        try {
            userBookRepository.count();
            System.out.println("Database keep-alive ping successful");
        } catch (Exception e) {
            System.out.println("Database keep-alive failed: " + e.getMessage());
        }
    }
}