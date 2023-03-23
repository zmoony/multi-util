package com.boot.redis.task;

import lombok.extern.log4j.Log4j2;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


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
            JobDetail RedisToAllJobDetail = JobBuilder.newJob(RedisToall.class)
                    .withIdentity("RedisToAllJobDetail","job_min").build();
            //todo 时间注意自定义
            Trigger trigger = newTrigger().withIdentity("RedisToAllJobtrigger", "wiscom")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(30)
                            .repeatForever()).build();
            scheduler.scheduleJob(RedisToAllJobDetail, trigger);
        }catch (Exception e){
            log.error("定时任务调度错误："+e.getMessage());
        }
    }
}
