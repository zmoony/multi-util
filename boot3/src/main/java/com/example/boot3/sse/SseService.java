package com.example.boot3.sse;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter connect(String uuid);

    void sendMessage(Message message);
}