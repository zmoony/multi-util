package com.boot.springkafka.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuez
 * @title: Message
 * @projectName muti-scaffold
 * @description: 定义消息实体
 * @date 2021/6/8 11:28
 */
@Data
public class Message implements Serializable{
    private String from;
    private String message;

    public Message() {
    }

    public Message(String from, String message) {
        this.from = from;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
