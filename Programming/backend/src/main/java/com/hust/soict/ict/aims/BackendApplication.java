package com.hust.soict.ict.aims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BackendApplication {
    static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
