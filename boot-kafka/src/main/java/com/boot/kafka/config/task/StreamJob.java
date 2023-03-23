package com.boot.kafka.config.task;

import com.boot.kafka.common.GlobalObject;
import com.boot.kafka.util.KafkaUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.kstream.KStream;
import org.quartz.*;

/**
 * stream流式处理
 *
 * @author yuez
 * @version 1.0.0
 * @className StreamJob
 * @date 2021/4/2 11:31
 **/
@Log4j2
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class StreamJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        stream();
    }

    private void stream() {
        log.info("开始启动kafka的流式操作");
        try {
            KafkaUtil.kafkaStreams.start();
        } catch (IllegalStateException e) {
            KafkaUtil.kafkaStreams.close();
            log.error("非法状态异常：{}",e);
        } catch (StreamsException e) {
            KafkaUtil.kafkaStreams.close();
            log.error("流式处理异常：{}",e);
        }catch (Exception e){
            KafkaUtil.kafkaStreams.close();
            log.error("流式处理总异常：{}",e);
        }
    }
}

