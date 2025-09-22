package com.boot.websocket.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * NotifyController
 * @MessageMapping("/send")客户端(浏览器)调用 /app/send 接口时自动对应到该方法。这里的 /app 前缀是在配置文件中配置的。@SendTo("/topic/notify")用于广播消息。
 * 任何订阅了 /topic/notify 的客户端都将收到此方法返回的消息（方法返回值）。
 *
 * 用户特定消息路由：允许向特定用户发送消息
 * @SendToUser注解支持：可以使用@SendToUser注解向特定用户发送消息
 * 用户目标解析：Spring会自动将/user/{username}/destination这样的路径解析为特定用户的私有通道
 * @author yuez
 * @since 2025/8/18
 */
@Controller
public class NotifyController {
    // 接受客户端请求（会自动拼接WebSocketConfig中配置的 /app 前缀）
    @MessageMapping("/send")
    // 该方法的返回值会自动广播到所有订阅了/topic/notify的客户端
    @SendTo("/topic/notify")
    public String send(String message) {
        // 广播消息
        return String.format("广播消息: %s", message);
    }

    // 向特定用户发送消息
    @MessageMapping("/private")
    @SendToUser("/queue/messages")
    public String sendPrivateMessage(String message, Principal principal) {
        // 这条消息只会发送给指定的用户
        return "这是私有消息: " + message;
    }

    // 或者使用MessagingTemplate发送私有消息
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendPrivateMessageToUser(String username, String message) {
        // 发送消息给特定用户
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/messages",
                "你好，这是专属于你的消息: " + message
        );
    }
}
