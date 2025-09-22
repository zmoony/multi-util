package com.boot.websocket.task;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * AutowiredTask
 *
 * @author yuez
 * @since 2024/11/27
 */
@Component
public class AutowiredTask {
    @Autowired
    private TaskScheduler taskScheduler;

    @PostConstruct
    public void start(){
       /* this.taskScheduler.schedule(()->{
            System.out.println("AutowiredTask ...");
        },new CronTrigger("0/2 * * * * ?"));

        this.taskScheduler.scheduleAtFixedRate(()->{
            System.out.println("固定周期指定任务") ;
        }, Duration.ofSeconds(2));*/
    }

}
