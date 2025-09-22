package com.boot.websocket.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocketConfig
 * 启用WebSocket功能，使用stomp协议
 *
 * @EnableWebSocketMessageBroker开启 WebSocket 消息处理，支持通过 STOMP 协议实现高效的消息传递，让服务器与客户端能双向实时通信。
 * enableSimpleBroker("/topic")启用内存消息代理，将带 /topic 前缀的消息（如 /topic/notify）自动广播给所有订阅该主题的客户端，实现消息的 "一对多" 推送。
 * setApplicationDestinationPrefixes("/app")设置客户端发送消息到服务器的统一前缀 /app。例如，客户端发送 /app/send 时，会触发使用 @MessageMapping("/send") 注解的方法。
 * addEndpoint("/ws")设置 WebSocket 连接入口为 /ws，前端通过 ws://localhost:8080/ws 建立实时通信会话。
 * setAllowedOriginPatterns("*")允许动态域名（但不能和 allowCredentials(true) 同时使用）。
 * withSockJS()启用 SockJS 兼容库，当浏览器不支持 WebSocket 时，自动降级使用 HTTP 流或长轮询，确保实时通信的可靠性。
 *
 * @author yuez
 * @since 2025/8/18
 */
@Configuration
// 开启 WebSocket 消息处理功能;
// 它允许使用 STOMP（简单文本定向消息协议，Simple Text Oriented Messaging Protocol）来处理通过 WebSocket 进行的消息传递。
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
// 广播消息访问前缀
        config.enableSimpleBroker("/topic");
// 客户端到服务器通信的前缀
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户专属前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
// WebSocket接口
                .addEndpoint("/ws")
                .setHandshakeHandler(new CustomHandshakeHandler())
// 跨域配置
                .setAllowedOriginPatterns("*")
// 启用SockJS以提供回退支持
                .withSockJS();
    }
}
