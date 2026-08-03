package com.buxbuddy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@EnableCaching
public class BuxBuddyApplication {

    public static void main(String[] args) {
        log.info("Starting BuxBuddy application...");
        SpringApplication.run(BuxBuddyApplication.class, args);
    }
}