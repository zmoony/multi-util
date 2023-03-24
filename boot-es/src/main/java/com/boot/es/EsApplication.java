package com.boot.es;

import com.boot.es.test.BulkTest2;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class EsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("启动成功");
    }
}
