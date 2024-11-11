package com.shop.ddstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(scanBasePackages = "com.shop.ddstore")
@EnableJpaAuditing
public class DdstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdstoreApplication.class, args);
    }

}
