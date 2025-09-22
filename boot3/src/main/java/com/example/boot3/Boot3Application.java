package com.example.boot3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
//@EnableScheduling
public class Boot3Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Boot3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("start success.....");
    }
}
