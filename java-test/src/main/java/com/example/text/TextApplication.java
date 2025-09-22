package com.example.text;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.cloud.openfeign.EnableFeignClients;

@Log4j2
//@EnableFeignClients
//springboot引入security依赖之后，默认访问资源需要登陆，不想用的话可以禁用，有两种方式
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TextApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TextApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.debug("****************debug****************");
        log.info("****************info****************");
    }
}
