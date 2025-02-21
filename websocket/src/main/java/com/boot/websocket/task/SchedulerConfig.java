package com.boot.websocket.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * SchedulerConfig
 * 默认单线程执行，需要配置线程池
 *  （java配置 优于  配置文件的配置）
 * 1. 采用配置i文件
 * # 定时任务配置
 * spring.task.scheduling.pool.size=5  # 设置定时任务线程池的大小
 * spring.task.scheduling.shutdown.await-termination=true  # 关闭时等待任务完成
 * spring.task.scheduling.shutdown.await-termination-period=30s  # 关闭时等待的时间
 *
 * @author yuez
 * @since 2024/11/27
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);  // 设置线程池大小为 20
        scheduler.setThreadNamePrefix("scheduled-task-");
        return scheduler;
    }
}
