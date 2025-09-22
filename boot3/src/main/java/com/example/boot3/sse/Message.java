package com.example.boot3.sse;

import lombok.Data;

@Data
public class Message {
    private String data;
    private Integer total;
}