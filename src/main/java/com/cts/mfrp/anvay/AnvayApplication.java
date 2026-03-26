package com.cts.mfrp.anvay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class AnvayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnvayApplication.class, args);
        System.out.println("----------------------------------------------");
        System.out.println("   ANVAY Multi-tenant System is Running!      ");
        System.out.println("----------------------------------------------");
    }
}