package com.boot.kafka.common;

import com.boot.kafka.config.KafkaConfig;
import lombok.extern.log4j.Log4j2;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 全局变量
 * <p>@PostConstruct 注解上无参的方法上</p>
 * @author yuez
 * @version 1.0.0
 * @className GlobalObject
 * @date 2021/3/17 14:28
 **/
@Service
@Log4j2
public class GlobalObject {

    @Autowired
    private KafkaConfig kafkaConfig;

    public static KafkaConfig KAFKA_INFO;
    public static final String MODEL_IDEMPOTENCE = "idempotence";
    public static final String MODEL_TRANSACTIONAL = "transactional";
    public static final String CONSUMEERROR_WAIT = "wait";
    public static final String CONSUMEERROR_SKIP = "skip";

    @PostConstruct
    public void init(){
        KAFKA_INFO = kafkaConfig;
        log.info("kafka配置读取成功");
    }

}
