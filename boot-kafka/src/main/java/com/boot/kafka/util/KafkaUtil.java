package com.boot.kafka.util;

import com.boot.kafka.common.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Properties;

/**
 * kafka工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className KafkaUtil
 * @date 2021/3/22 17:20
 **/
@Log4j2
@Service
public class KafkaUtil {
    //幂等模式
    public static Producer<String, String> producer_idem;
    //事务模式
    public static Producer<String, String> producer_tran;
    //消费者
    public static KafkaConsumer<String, String> consumer;
    //流
    public static  KafkaStreams kafkaStreams;
    //配置文件
    public static Properties props_pro = new Properties();
    public static Properties props_consume = new Properties();
    public static Properties props_stream = new Properties();


    static {
        /********初始化生产者**********/
        if(GlobalObject.KAFKA_INFO.isOpenProduce()){
            securityConfig(props_pro);
            props_pro.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GlobalObject.KAFKA_INFO.getBootstrapServers());
            props_pro.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,GlobalObject.KAFKA_INFO.isEnableIdempotence());
            props_pro.put(ProducerConfig.ACKS_CONFIG,GlobalObject.KAFKA_INFO.getAcks());
            props_pro.put(ProducerConfig.RETRIES_CONFIG,GlobalObject.KAFKA_INFO.getRetries());
            props_pro.put(ProducerConfig.BATCH_SIZE_CONFIG,GlobalObject.KAFKA_INFO.getBatchSize());
            props_pro.put(ProducerConfig.LINGER_MS_CONFIG,GlobalObject.KAFKA_INFO.getLingerMs());
            props_pro.put(ProducerConfig.BUFFER_MEMORY_CONFIG,GlobalObject.KAFKA_INFO.getBufferMemory());
            props_pro.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
            props_pro.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            produce(props_pro);
        }
        /********初始化消费者**********/
        if(GlobalObject.KAFKA_INFO.isOpenConsume()){
            securityConfig(props_consume);
            props_consume.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, GlobalObject.KAFKA_INFO.getBootstrapServers());
            props_consume.put(ConsumerConfig.GROUP_ID_CONFIG, GlobalObject.KAFKA_INFO.getGroupId());
            props_consume.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, GlobalObject.KAFKA_INFO.getMaxPollRecords());
            props_consume.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, GlobalObject.KAFKA_INFO.getSessionTimeoutMs());
            props_consume.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, GlobalObject.KAFKA_INFO.isEnableAutoCommit());
            //latest,earliest
            props_consume.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, GlobalObject.KAFKA_INFO.getAutoOffsetReset());
            props_consume.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            props_consume.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            //事务模式启用：isolation.level，“read_uncommitted（默认）”：消费端应用可以看到（消费到）未提交的事务；“read_committed”：消费端应用不可以看到尚未提交的事务内的消息。
//            props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG,"read_committed");
//            consume(props_consume);
        }
        /********初始化流信息**********/
        if(GlobalObject.KAFKA_INFO.isOpenStream()){
            securityConfig(props_stream);
            props_stream.put(StreamsConfig.APPLICATION_ID_CONFIG, GlobalObject.KAFKA_INFO.getApplicationId());
            props_stream.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,GlobalObject.KAFKA_INFO.getBootstrapServers());
            props_stream.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, GlobalObject.KAFKA_INFO.getCacheMaxBytesBuffering());
            props_stream.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props_stream.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props_stream.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,GlobalObject.KAFKA_INFO.getAutoOffsetReset());
            props_stream.put(StreamsConfig.REPLICATION_FACTOR_CONFIG,GlobalObject.KAFKA_INFO.getReplicationFactor());
            props_stream.put(StreamsConfig.STATE_DIR_CONFIG,GlobalObject.KAFKA_INFO.getStateDir());
            stream(props_stream);
        }
    }

    /***安全模式****/
    private static void securityConfig(Properties props){
        if(GlobalObject.KAFKA_INFO.isSecurity()){
            props.put("java.security.auth.login.config",GlobalObject.KAFKA_INFO.getSecurityJavaConfig());
//            props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required   username=\"kfk\"   password=\"kfkpwd\";");
            props.put("security.protocol",GlobalObject.KAFKA_INFO.getSecurityProtocol());
            props.put("sasl.mechanism",GlobalObject.KAFKA_INFO.getSaslMechanism());
        }
    }

    private static void produce(Properties props){
        switch (GlobalObject.KAFKA_INFO.getModel()){
            case GlobalObject.MODEL_IDEMPOTENCE:
                producer_idem = new KafkaProducer<>(props);
                log.info("等幂模式Kafka生产者初始化成功");
                break;
            case GlobalObject.MODEL_TRANSACTIONAL:
                props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,GlobalObject.KAFKA_INFO.getTransactionalId());
                producer_tran = new KafkaProducer<>(props);
                producer_tran.initTransactions();
                log.info("事务模式Kafka生产者初始化成功");
                break;
            default:
                producer_idem = new KafkaProducer<>(props);
                log.info("等幂模式Kafka生产者初始化成功");
        }
    }

    private static void consume(Properties props){
        consumer = new KafkaConsumer<>(props);
        log.info("消费者初始化成功");
    }

    private static void stream(Properties props){
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Object, Object> stream = streamsBuilder.stream(GlobalObject.KAFKA_INFO.getTopicSrc());
        KStream<Object, Object> mapValues = stream.mapValues(value -> {
            return value;
        });
        mapValues.filter((key,value)->{return true;}).to(GlobalObject.KAFKA_INFO.getTopicDesc());
        kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
    }

    public static void resetConsume(){
        consume(props_consume);
    }
}
