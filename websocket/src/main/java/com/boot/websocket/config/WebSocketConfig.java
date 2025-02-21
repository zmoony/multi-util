package com.boot.websocket.config;

import com.boot.websocket.service.MyWebSocketHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * WebSocketConfig
 *
 * @author yuez
 * @since 2024/10/17
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig  implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
       /* registry.addHandler(toolShellTermHeander, "/tool/shell/term")
                .addInterceptors(new ToolShellTermInterceptor()).setAllowedOrigins("*");*/
        webSocketHandlerRegistry.addHandler(new MyWebSocketHandler(), "/ws/text/simple").setAllowedOrigins("*");
    }
    //订阅主题
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 配置消息代理，允许客户端订阅 /topic 下的主题
        config.enableSimpleBroker("/topic");
        // 配置应用前缀，客户端发送消息时需要使用 /app 前缀
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点，并启用SockJS支持
//        registry.addEndpoint("/ws/stomp").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/ws/stomp").setAllowedOriginPatterns("*").withSockJS();
    }
    @PostConstruct
    public void init() {
        System.out.println("WebSocketConfig initialized");
    }


    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(819200);
        return container;
    }


}
