package com.boot.websocket.client.java;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

/**
 * 创建一个robust的WebSocket客户端，简单模式
 */
public class RobustWebSocketClient {
    public static void main(String[] args) throws Exception {
        String uri = "ws://localhost:8080/websocket/user123";

        // 创建CountDownLatch用于等待
        CountDownLatch latch = new CountDownLatch(1);

        // 创建监听器
        WebSocketListener listener = new WebSocketListener(latch);

        // 创建HttpClient
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // 建立WebSocket连接
        CompletableFuture<WebSocket> future = client.newWebSocketBuilder()
                .buildAsync(URI.create(uri), listener);

        // 等待连接建立
        WebSocket webSocket = future.join();
        System.out.println("WebSocket连接已建立");

        // 发送消息
        webSocket.sendText("Hello from Java client", true);
        System.out.println("已发送消息");

        // 等待一段时间或直到连接关闭
        try {
            if (!latch.await(30, java.util.concurrent.TimeUnit.SECONDS)) {
                System.out.println("超时，关闭连接");
            }
        } catch (InterruptedException e) {
            System.out.println("等待被中断");
        } finally {
            // 确保连接被关闭
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client closing").join();
            System.out.println("连接已关闭");
        }
    }

    static class WebSocketListener implements Listener {
        private final CountDownLatch latch;

        public WebSocketListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("WebSocket连接已打开");
            // 请求更多消息
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            System.out.println("收到文本消息: " + data);

            // 处理完当前消息后请求下一个消息
            webSocket.request(1);
            return CompletableFuture.completedStage(null);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("连接关闭: " + reason + " (状态码: " + statusCode + ")");
            // 释放等待锁
            latch.countDown();
            return CompletableFuture.completedStage(null);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.err.println("WebSocket错误: " + error.getMessage());
            error.printStackTrace();
            // 释放等待锁
            latch.countDown();
        }
    }
}
