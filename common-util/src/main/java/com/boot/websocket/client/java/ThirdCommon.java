/*
package com.boot.websocket.client.java;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

*/
/**
 * <dependency>
 *     <groupId>org.java-websocket</groupId>
 *     <artifactId>Java-WebSocket</artifactId>
 *     <version>1.5.3</version>
 * </dependency>
 *//*

public class ThirdPartyWebSocketClient extends WebSocketClient {

    public ThirdPartyWebSocketClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("连接已建立");
        // 连接成功后发送消息
        send("Hello Server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("收到消息: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("连接关闭: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("发生错误: " + ex.getMessage());
    }

    public static void main(String[] args) {
        try {
            String uri = "ws://localhost:8080/websocket/user123";
            ThirdPartyWebSocketClient client = new ThirdPartyWebSocketClient(uri);

            // 连接服务器
            client.connect();

            // 等待连接建立
            while (!client.isOpen()) {
                Thread.sleep(100);
            }

            // 发送消息
            client.send("Hello from Java client");

            // 保持连接一段时间
            Thread.sleep(10000);

            // 关闭连接
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
