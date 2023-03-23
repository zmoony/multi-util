package com.boot.springkafka.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author yuez
 * @title: KafkaMessageListener
 * @projectName muti-scaffold
 * @description: 消息消费
 * @date 2021/6/8 11:16
 */
@Log4j2
@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "wiscom.test1,wiscom.test2",groupId = "test-consumer")
    public void listen(String message){
        log.info("接受信息：{}",message);
    }

    /**
     * 获取消息来自哪个分区
     * @param message
     * @param partition
     */
    @KafkaListener(topics = "test", groupId = "test-consumer")
    public void listen(@Payload String message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition){
        log.info("接收消息: {}，partition：{}",message,partition);
    }

    /**
     * 只接收来自特定分区
     * @param message
     * @param partition
     */
    @KafkaListener(groupId = "test-consumer",
            topicPartitions = @TopicPartition(topic = "test", partitionOffsets = {
                    @PartitionOffset(partition = "0",initialOffset = "0")}))
    public void listen2(@Payload String message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition){

    }

    /**
     * 不需要指定initialOffset
     * @param message
     * @param partition
     */
    @KafkaListener(groupId = "test-consumer",
            topicPartitions = @TopicPartition(topic = "test", partitions = { "0", "1" }))
    public void listen3(@Payload String message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition){

    }

}
