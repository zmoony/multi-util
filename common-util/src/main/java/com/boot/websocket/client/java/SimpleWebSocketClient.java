package com.boot.websocket.client.java;

import java.net.URI;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 你的项目必须使用低于 Java 11 的版本，就需要使用第三方 WebSocket 库（如 Tyrus、Jetty 等）
 */
public class SimpleWebSocketClient {
    public static void main(String[] args) throws Exception {
        // WebSocket服务器地址
        String uri = "ws://localhost:8080/websocket/user123";

        // 创建WebSocket客户端
        SimpleWebSocketListener listener = new SimpleWebSocketListener();
        // 使用HttpClient创建WebSocket（这是JDK 11中的正确方式）
        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();

        CompletableFuture<WebSocket> webSocketFuture = httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(uri), listener);

        // 等待连接建立
        WebSocket webSocket = webSocketFuture.join();
        System.out.println("WebSocket连接已建立");

        // 发送消息
        webSocket.sendText("Hello Server", true).join();

        // 保持连接一段时间
        Thread.sleep(10000);

        // 关闭连接
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Goodbye").join();
        System.out.println("WebSocket连接已关闭");
    }

    static class SimpleWebSocketListener implements Listener {
        private WebSocket webSocket;

        @Override
        public void onOpen(WebSocket webSocket) {
            this.webSocket = webSocket;
            System.out.println("连接已建立");
            Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("收到消息: " + data);
            return Listener.super.onText(webSocket, data, last);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("连接关闭: " + reason);
            return Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.err.println("发生错误: " + error.getMessage());
            Listener.super.onError(webSocket, error);
        }
    }
}
