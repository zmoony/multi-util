package com.boot.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * WebSocketApplication
 *
 * @author yuez
 * @since 2024/10/17
 */
@SpringBootApplication
public class WebSocketApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(WebSocketApplication.class, args);
    }
}
