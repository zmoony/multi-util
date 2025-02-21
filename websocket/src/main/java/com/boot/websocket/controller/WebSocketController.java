package com.boot.websocket.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Controller
public class WebSocketController {

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        // 处理接收到的消息，并返回处理结果
        String replyMessage = "【订阅-处理后】处理消息：" + message;
        log.info("接收订阅消息并指定主题发送：{}",message);
        return replyMessage;
    }
}
