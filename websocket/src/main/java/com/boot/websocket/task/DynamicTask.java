package com.boot.websocket.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * DynamicTask
 * 动态增减任务
 * 实现SchedulingConfigurer
 *
 * @author yuez
 * @since 2024/11/27
 */
@Configuration
public class DynamicTask implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler() ;
//        taskScheduler.setThreadNamePrefix("my-task-") ;
//        taskScheduler.afterPropertiesSet();
//        taskRegistrar.setTaskScheduler(taskScheduler );

//        taskRegistrar.addCronTask(() -> {
//            System.out.println("动态注册调度任务...");
//        }, "*/2 * * * * *");
    }
}
