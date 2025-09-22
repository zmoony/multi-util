/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wiscom.redis;

import com.wiscom.redis.pool.RedisPool;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

/**
 *
 * @author Administrator
 */
@SpringBootApplication
@Log4j2
public class Application implements CommandLineRunner{
	@Override
	public void run(String... strings) throws Exception {

	}
	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}

}
