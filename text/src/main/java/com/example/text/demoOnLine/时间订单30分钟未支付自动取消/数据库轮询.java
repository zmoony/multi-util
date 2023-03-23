package com.example.text.demoOnLine.时间订单30分钟未支付自动取消;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * JOB  必须是public的类
 * 优点：简单易行，支持集群操作
 * 缺点：内存消耗大；最坏延迟3秒；数据量大的时候，数据库消耗大
 */
public class 数据库轮询 implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("数据库轮询");
    }

   public void init() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(数据库轮询.class).withIdentity("DataBasePolling","g1").build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("DataBasePolling","g1").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();
    }

    public static void main(String[] args) throws SchedulerException {
        new 数据库轮询().init();
    }
}