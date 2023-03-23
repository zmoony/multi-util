package com.boot.kafka.config.task;

import com.boot.kafka.common.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import static org.quartz.TriggerKey.triggerKey;


/**
 * 定时任务管理器
 * JobDetail ConsumerJobDetail = JobBuilder.newJob(ConsumerJob.class)
 *                     .withIdentity("ConsumerJob","job_min").build();
 * CronScheduleBuilder cron_min = CronScheduleBuilder.cronSchedule("0 0 1 * * ? ");
 *             CronTrigger cronTrigger_min =TriggerBuilder.newTrigger()
 *                     .withIdentity("ConsumerJob","tri_min")
 *                     .withSchedule(cron_day).startNow().build();
 *  scheduler.scheduleJob(jobDetail_min, trigger);
 * @author yuez
 * @version 1.0.0
 * @className SchedulerConfig
 * @date 2021/3/23 19:19
 **/
@Component
@Log4j2
public class SchedulerConfig {
    @Autowired
    private Scheduler scheduler;
    @PostConstruct
    public void execute(){
        try {
            scheduler.start();
            log.info("调度器启动成功");
            //DisallowConcurrentExecution 根据jobdetail来定义实例，不允许并发
            JobDetail ConsumerJobDetail = JobBuilder.newJob(ConsumerJob.class)
                    .withIdentity("ConsumerJob","job_min").build();
            JobDetail StreamJobDetail = JobBuilder.newJob(StreamJob.class)
                    .withIdentity("StreamJob","job_min").build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey("myTrigger", "myTriggerGroup"))
                    .withSchedule( SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(30)
                            .repeatForever())
                    .startNow()
//                    .startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.MILLISECOND))
                    .build();
            if(GlobalObject.KAFKA_INFO.isOpenConsume()){
                log.info("消费者任务初始化成功");
                scheduler.scheduleJob(ConsumerJobDetail, trigger);
            }
            if(GlobalObject.KAFKA_INFO.isOpenStream()){
                log.info("流任务初始化成功");
                scheduler.scheduleJob(StreamJobDetail, trigger);
            }

        }catch (Exception e){
            log.error("定时任务调度错误："+e.getMessage());
        }
    }
}
