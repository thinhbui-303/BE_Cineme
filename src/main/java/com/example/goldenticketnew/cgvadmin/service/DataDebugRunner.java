package com.example.goldenticketnew.cgvadmin.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataDebugRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DATA DEBUG FINISHED ===");
    }
}
