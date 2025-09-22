package com.example.boot3.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 默认一个线程在跑,需要配置taskSchedule
 */
@Component
public class ScheduleTreadTest {

    @Scheduled(cron = "${app.cron.expression-1}")
    public void task01() {
        System.out.println(Thread.currentThread().getName() + "----> task01");
    }

    @Scheduled(cron = "${app.cron.expression-2}")
    public void task02() {
        System.out.println(Thread.currentThread().getName() + "----> task02");
    }

    @Scheduled(cron = "${app.cron.expression-3}")
    public void task03() {
        System.out.println(Thread.currentThread().getName() + "----> task03");
    }

}