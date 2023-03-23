package com.boot.springkafka.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author yuez
 * @title: SendMessageController
 * @projectName muti-scaffold
 * @description: 发送消息测试
 * @date 2021/6/8 10:14
 */
@Log4j2
@RestController
public class SendMessageController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("send/{message}")
    public void sendMessage(@PathVariable String message ) throws Exception {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("wiscom.test1", message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息：{} 发送失败，原因：{}", message, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("成功发送消息：{}，offset=[{}]", message, result.getRecordMetadata().offset());
            }
        });
    }
}
