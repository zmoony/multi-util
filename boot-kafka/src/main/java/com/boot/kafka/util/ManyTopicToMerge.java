package com.boot.kafka.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ManyTopicToMerge {
    public static void main(String[] args) {
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "kafka:9092");
        consumerProps.put("group.id", "topic-merger-group");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("auto.offset.reset", "earliest");

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "kafka:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        // 源topic列表
        List<String> sourceTopics = Arrays.asList("topic1", "topic2", "topic3");
        String targetTopic = "merged-topic";

        try {
            // 订阅源topics
            consumer.subscribe(sourceTopics);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                List<ProducerRecord<String, String>> batchRecords = new ArrayList<>();

                for (ConsumerRecord<String, String> record : records) {
                    // 构建新消息，可以添加源topic信息
                    String newValue = String.format("{\"sourceTopic\":\"%s\",\"data\":%s}",
                            record.topic(), record.value());

                    batchRecords.add(new ProducerRecord<>(targetTopic,
                            record.key(), newValue));

                    if (batchRecords.size() >= 100) {
                        sendBatch(producer, batchRecords);
                        batchRecords.clear();
                    }
                }

                // 发送剩余的消息
                if (!batchRecords.isEmpty()) {
                    sendBatch(producer, batchRecords);
                }

                // 手动提交offset
                consumer.commitSync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
            consumer.close();
        }
    }

    private static void sendBatch(KafkaProducer<String, String> producer,
                                  List<ProducerRecord<String, String>> records) {
        List<Future<RecordMetadata>> futures = new ArrayList<>();

        for (ProducerRecord<String, String> record : records) {
            futures.add(producer.send(record));
        }

        // 等待所有消息发送完成
        for (Future<RecordMetadata> future : futures) {
            try {
                future.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
