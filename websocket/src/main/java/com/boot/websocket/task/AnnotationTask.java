package com.boot.websocket.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * AnnotationTask
 *
 * @author yuez
 * @since 2024/11/27
 */
@Component
public class AnnotationTask {

    @Scheduled(cron = "0/5 * 12 * * ?")
    public void start() {
        System.out.println("AnnotationTask start");
    }

    /**
     * fixedRate：任务的执行间隔是固定的，不考虑任务的实际执行时间。如果任务执行时间较长，可能会导致任务重叠。
     */
    @Scheduled(fixedRate = 500000)
    public void start2() {
        System.out.println("AnnotationTask start2 fixedRate");
    }

    /**
     * fixedDelay：任务的执行间隔是从上一次任务结束的时间点开始计算的，确保任务不会重叠。
     */
    @Scheduled(fixedDelay = 500000)
    public void start3() {
        System.out.println("AnnotationTask start3 fixedDelay");
    }
}
