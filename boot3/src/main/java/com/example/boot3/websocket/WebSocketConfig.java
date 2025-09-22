package com.example.boot3.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocketConfig
 *
 * @author yuez
 * @since 2024/3/27
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndPointExporter() {
        return new ServerEndpointExporter ();
    }
}
