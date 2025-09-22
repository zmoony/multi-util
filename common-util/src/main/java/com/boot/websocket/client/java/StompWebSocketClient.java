package com.boot.websocket.client.java;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 *<dependency>
 *     <groupId>org.springframework</groupId>
 *     <artifactId>spring-websocket</artifactId>
 *     <version>5.3.21</version>
 * </dependency>
 * <dependency>
 *     <groupId>org.springframework</groupId>
 *     <artifactId>spring-messaging</artifactId>
 *     <version>5.3.21</version>
 * </dependency>
 * <dependency>
 *     <groupId>com.fasterxml.jackson.core</groupId>
 *     <artifactId>jackson-databind</artifactId>
 *     <version>2.13.3</version>
 * </dependency>
 *
 */
public class StompWebSocketClient {

    public static void main(String[] args) throws Exception {
        // 创建WebSocket传输
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        // 创建STOMP客户端
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // 连接URL
        String url = "ws://localhost:8080/ws"; // 对应STOMP端点

        // 创建会话处理器
        StompSessionHandler sessionHandler = new MyStompSessionHandler();

        // 连接到服务器
        StompSession stompSession = stompClient.connect(url, sessionHandler).get();

        // 等待连接建立
        Thread.sleep(1000);

        // 订阅广播消息
        stompSession.subscribe("/topic/notify", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("收到广播消息: " + payload);
            }
        });

        // 订阅私信消息
        stompSession.subscribe("/user/queue/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("收到私信: " + payload);
            }
        });

        // 发送消息
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("输入消息 (输入'quit'退出): ");
            String message = scanner.nextLine();

            if ("quit".equals(message)) {
                break;
            }

            // 发送广播消息
            stompSession.send("/app/send", message);
        }

        // 关闭连接
        stompSession.disconnect();
        System.exit(0);
    }

    private static class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("STOMP连接已建立");
        }

        @Override
        public void handleException(StompSession session, StompCommand command,
                                  StompHeaders headers, byte[] payload, Throwable exception) {
            System.err.println("处理STOMP消息时发生异常: " + exception.getMessage());
            exception.printStackTrace();
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            System.err.println("WebSocket传输错误: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
