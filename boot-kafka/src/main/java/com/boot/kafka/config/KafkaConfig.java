package com.boot.kafka.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * kafka的配置类读取
 *
 * @author yuez
 * @version 1.0.0
 * @className KafkaConfig
 * @date 2021/3/17 14:25
 **/
@Data
@Log4j2
@ConfigurationProperties(ignoreUnknownFields = true,prefix = "wiscom.kafka")
@Configuration
@Component
public class KafkaConfig {
    //安全模式
    private boolean security;
    private String securityJavaConfig;
    private String securityProtocol;
    private String saslMechanism;
    //通用
    private String bootstrapServers;
    private String topicSrc;
    private String topicDesc;
    private String autoOffsetReset;
    //生产者
    private boolean openProduce;
    private String model;
    private boolean enableIdempotence;
    private String acks;
    private String retries;
    private String batchSize;
    private String lingerMs;
    private String bufferMemory;
    private String transactionalId;
    private String idempotenceKey;
    //消费者
    private boolean openConsume;
    private boolean enableAutoCommit;
    private String autoCommitIntervalMs;
    private String groupId;
    private String sessionTimeoutMs;
    private String maxPollRecords;
    private String consumeError;
    private String consumeTime;
    //流处理
    private boolean openStream;
    private String applicationId;
    private Long cacheMaxBytesBuffering;
    private String replicationFactor;
    private String stateDir;

}
