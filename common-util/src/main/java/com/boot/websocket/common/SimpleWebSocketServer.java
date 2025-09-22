package com.boot.websocket.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint("/ws/simple/{userId}")
public class SimpleWebSocketServer {

    // 存储在线用户数
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // 存储所有连接的会话
    private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    // 当前会话的用户ID
    private String userId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        this.userId = userId;
        sessions.put(userId, session);
        onlineCount.incrementAndGet();

        log.info("用户 {} 连接成功，当前在线人数: {}", userId, onlineCount.get());

        // 发送欢迎消息
        sendMessage(session, "欢迎用户 " + userId + " 连接成功！");
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("来自用户 {} 的消息: {}", userId, message);

        // 处理消息 - 这里可以实现你的业务逻辑
        handleMessage(message, session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        sessions.remove(userId);
        onlineCount.decrementAndGet();
        log.info("用户 {} 断开连接，当前在线人数: {}", userId, onlineCount.get());
    }

    /**
     * 发生错误时调用的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户 {} 发生错误", userId, error);
    }

    /**
     * 处理收到的消息
     */
    private void handleMessage(String message, Session session) {
        try {
            // 解析消息格式，例如: {"to":"user2","msg":"hello"}
            if (message.startsWith("{")) {
                // JSON格式消息处理
                // 这里可以使用Jackson或其他JSON库解析
                // 为了简化示例，我们用字符串处理
                if (message.contains("\"to\":")) {
                    // 私聊消息
                    String toUser = extractToUser(message);
                    String content = extractMessageContent(message);
                    sendToUser(toUser, "来自 " + userId + " 的私信: " + content);
                } else {
                    // 广播消息
                    broadcastMessage("用户 " + userId + " 说: " + message);
                }
            } else {
                // 简单文本消息 - 广播
                broadcastMessage("用户 " + userId + " 说: " + message);
            }
        } catch (Exception e) {
            log.error("处理消息时出错", e);
            sendMessage(session, "消息处理失败: " + e.getMessage());
        }
    }

    /**
     * 发送消息给指定用户
     */
    private void sendToUser(String toUserId, String message) {
        Session session = sessions.get(toUserId);
        if (session != null && session.isOpen()) {
            sendMessage(session, message);
        } else {
            log.warn("用户 {} 不在线，无法发送消息", toUserId);
        }
    }

    /**
     * 广播消息给所有用户
     */
    private void broadcastMessage(String message) {
        for (Session session : sessions.values()) {
            if (session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
    }

    /**
     * 简单提取目标用户（示例）
     */
    private String extractToUser(String message) {
        // 简化的JSON解析示例
        int start = message.indexOf("\"to\":\"") + 6;
        int end = message.indexOf("\"", start);
        return message.substring(start, end);
    }

    /**
     * 简单提取消息内容（示例）
     */
    private String extractMessageContent(String message) {
        // 简化的JSON解析示例
        int start = message.indexOf("\"msg\":\"") + 7;
        int end = message.indexOf("\"", start);
        return message.substring(start, end);
    }
}
