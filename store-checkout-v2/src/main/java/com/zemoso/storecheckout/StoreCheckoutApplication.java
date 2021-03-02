package com.zemoso.storecheckout;

import com.zemoso.storecheckout.service.impl.CheckoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreCheckoutApplication implements CommandLineRunner {

    @Autowired
    private CheckoutServiceImpl service;

    public static void main(String[] args) {
        SpringApplication.run(StoreCheckoutApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        service.startCheckoutApp();
    }
}
