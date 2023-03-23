package com.boot.kafka.dao;

import com.boot.kafka.common.GlobalObject;
import com.boot.kafka.util.KafkaUtil;
import com.boot.util.mapUtil.ObjectMapperUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 生产者的操作集合
 *
 * @author yuez
 * @version 1.0.0
 * @className ProducerDao
 * @date 2021/3/23 15:26
 **/
@Log4j2
@Repository
public class ProducerDao {
    //等幂模式
    public boolean sendBathMessageIdem(List<Map<String, Object>> list, String topic) {
        String idempotenceKey = GlobalObject.KAFKA_INFO.getIdempotenceKey();
        try {
            if ("null".equals(idempotenceKey)) {
                for (Map<String, Object> map : list) {
                    Future<RecordMetadata> send = KafkaUtil.producer_idem.send(
                            new ProducerRecord<>(topic, ObjectMapperUtil.obj2json(map)));
                    log.info(send.get());
                }
            } else {
                for (Map<String, Object> map : list) {
                    String key = String.valueOf(map.getOrDefault(idempotenceKey, null));
                    if("null".equals(key)){
                        Future<RecordMetadata> send = KafkaUtil.producer_idem.send(
                                new ProducerRecord<>(topic,ObjectMapperUtil.obj2json(map)));
                        log.info(send.get());
                        continue;
                    }
                    Future<RecordMetadata> send = KafkaUtil.producer_idem.send(
                            new ProducerRecord<>(topic,key,ObjectMapperUtil.obj2json(map)));
                    log.info(send.get());
                }
            }
        } catch (Exception e) {
            log.error("发生异常，终止此幂等模式：{}",e);
            KafkaUtil.producer_idem.close();
            return false;
        }
        return true;
    }

    //事务模式
    public boolean sendBathMessageTran(List<Map<String,Object>> list,String topic){
        String idempotenceKey = GlobalObject.KAFKA_INFO.getIdempotenceKey();
        KafkaUtil.producer_tran.beginTransaction();
        try {
            if ("null".equals(idempotenceKey)) {
                for (Map<String, Object> map : list) {
                    Future<RecordMetadata> send = KafkaUtil.producer_tran.send(
                            new ProducerRecord<>(topic, ObjectMapperUtil.obj2json(map)));
                    log.info(send.get());
                }
            } else {
                for (Map<String, Object> map : list) {
                    String key = String.valueOf(map.getOrDefault(idempotenceKey, null));
                    if("null".equals(key)){
                        Future<RecordMetadata> send = KafkaUtil.producer_tran.send(
                                new ProducerRecord<>(topic,ObjectMapperUtil.obj2json(map)));
                        log.info(send.get());
                        continue;
                    }
                    Future<RecordMetadata> send = KafkaUtil.producer_tran.send(
                            new ProducerRecord<>(topic,key,ObjectMapperUtil.obj2json(map)));
                    log.info(send.get());

                }
            }
            KafkaUtil.producer_tran.commitTransaction();
        } catch (Exception e) {
            log.error("发生异常，终止此幂等模式：{}",e);
            KafkaUtil.producer_tran.abortTransaction();
            KafkaUtil.producer_tran.close();
            return false;
        }
        return true;
    }
}
