/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.kafka.config.task;


import com.boot.kafka.common.GlobalObject;
import com.boot.kafka.common.RequestBean;
import com.boot.kafka.util.KafkaUtil;
import com.boot.util.common.CollectionUtils;
import com.boot.util.http.HttpUtils;
import com.boot.util.mapUtil.ObjectMapperUtil;
import com.boot.util.properties.PropertiesCustomUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author yuez
 * @DisallowConcurrentExecution //不允许多实例运行，确保唯一性
 */
@Log4j2
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class ConsumerJob implements Job {
    private HttpUtils httpUtils = new HttpUtils();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        startConsumer();
    }

    private void startConsumer() {
//        String path = this.getClass().getResource("/").getPath();
        KafkaUtil.resetConsume();
        KafkaConsumer<String, String> consumer = KafkaUtil.consumer;
        PropertiesCustomUtil pccu = new PropertiesCustomUtil("application-kafka.properties");
        PropertiesCustomUtil business = new PropertiesCustomUtil("application-business.properties");
        if (!"null".equals(GlobalObject.KAFKA_INFO.getConsumeTime()) &&
                GlobalObject.KAFKA_INFO.getConsumeTime().length() == 14) {
            consumeOnTime(consumer, pccu);
        } else {
            //默认处理
            consumer.subscribe(Arrays.asList(GlobalObject.KAFKA_INFO.getTopicSrc()));
        }
        dataProcess(consumer, business);
    }

    /**
     * 　　基于时间处理
     * <p>基于时间戳查询消息，consumer 订阅 topic 的方式必须是 Assign,当消费完最后一条数据，以后的订阅模式都由assign改为subscribe。</p>
     *
     * @param consumer 消费者
     * @throws
     * @author yuez
     * @date 2021/3/29 15:50
     */
    private void consumeOnTime(KafkaConsumer<String, String> consumer, PropertiesCustomUtil pccu) {
        log.info("开始时间：" + GlobalObject.KAFKA_INFO.getConsumeTime());
        LocalDateTime ldt = LocalDateTime.parse(GlobalObject.KAFKA_INFO.getConsumeTime(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        //拿到分区数据
        List<PartitionInfo> lpi = consumer.partitionsFor(GlobalObject.KAFKA_INFO.getTopicSrc());
        Map<TopicPartition, Long> timestampsToSearch = new HashMap<>();
        List<TopicPartition> ltp = new ArrayList<>();
        //构造待查询的分区
        for (PartitionInfo pi : lpi) {
            TopicPartition tp = new TopicPartition(GlobalObject.KAFKA_INFO.getTopicSrc(), pi.partition());
            timestampsToSearch.put(tp, ldt.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
            ltp.add(tp);
        }
        //分配,不会修改groupid的offset,不受group.id限制
        consumer.assign(ltp);
        //根据时间戳查找给定偏移量，时间戳大于第一条目标数据，则返回null;会返回时间大于等于查找时间的第一个偏移量
        Map<TopicPartition, OffsetAndTimestamp> mtp = consumer.offsetsForTimes(timestampsToSearch);
        boolean time_errror = false;
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : mtp.entrySet()) {
            if (entry.getValue() != null) {
                //重置偏移量
                consumer.seek(entry.getKey(), entry.getValue().offset());
            } else {
                //防止时间配置超前
                time_errror = true;
                break;
            }
        }

        if (time_errror) {
            consumer.subscribe(Arrays.asList(GlobalObject.KAFKA_INFO.getTopicSrc()));
        } else {
            //删除配置中的时间，防止重启又重复设置

            if (pccu.setProperty("wiscom.kafka.consumeTime", "10086")) {
                //已测试过，更改配置文件，不会变成只有consumer.time
                log.info("config/application-kafka.properties 配置文件修改成功");
            } else {
                log.info("config/application-kafka.properties 配置文件修改失败");
                System.exit(0);
            }
        }
    }

    /**
    　　拉取消息并发送数据，<p>错误消息按照策略执行</p>
    * @param consumer 消费者
    * @param business 读取发送信息的配置文件
    * @throws
    * @author yuez
    * @date 2021/4/2 11:08
    */
    private void dataProcess(KafkaConsumer<String, String> consumer, PropertiesCustomUtil business) {
        List<Map<String, Object>> list = new ArrayList<>();
        String url = business.getProperty("business.http.url");
        String authnom = business.getProperty("business.http.authnom");
        String type = business.getProperty("business.http.type");
        RequestBean requestBean = new RequestBean();
        while (true) {
            try {
                consumeData(list);
            } catch (Exception e) {
                log.error("拉取数据出错：{}",e);
            }
            if (list.size() > 0) {
                //发送数据
                boolean result = false;
                requestBean.setAuthnom(authnom).setType(type).setContent(list);
                try {
                    String s = httpUtils.doPost(url, null, ObjectMapperUtil.obj2json(requestBean), "utf-8");
                    if ("ok".equals(s)) {
                        result = true;
                    }
                    list.clear();
                    if (result) {
                        try {
                            consumer.commitSync();
                            log.info("此次消费成功");
                        } catch (CommitFailedException ex) {
                            log.error("偏移量提交失败：" + ex.getMessage());
                            //暂时直接关闭，后续再处理
                            consumer.close();
                            log.error("Kafka消费超时严重");
                            log.info("Kafka消费者关闭");
                            break;
                        }
                    } else {
                        if ("wait".equals(GlobalObject.KAFKA_INFO.getConsumeError())) {
                            consumer.close();
                            log.info("Kafka消费者关闭");
                            break;
                        } else {
                            //skip 跳过这个消息继续发送
                            consumer.commitSync();
                            log.info("消费失败，跳过此次消费的数据");
                        }
                    }
                } catch (Exception e) {
                    log.error("消费数据异常：{}", e);
                }
            }

        }
    }

    /**
    　　拉取kafka消息
    * @param list 结果合集
    * @throws
    * @author yuez
    * @date 2021/4/2 11:07
    */
    private void consumeData(List<Map<String, Object>> list) throws Exception {
        //time_out:毫秒
        ConsumerRecords<String, String> records = KafkaUtil.consumer.poll(Duration.ofMillis(200));
        if (records.count() > 0) {
            log.info("此次消费数据量：" + records.count());
        }
        forRecords:
        for (ConsumerRecord<String, String> record : records) {
            Map<String,Object> map = ObjectMapperUtil.json2map(record.value());
            if(CollectionUtils.isNotEmpty(map)){
                list.add(map);
            }
        }
    }
}


