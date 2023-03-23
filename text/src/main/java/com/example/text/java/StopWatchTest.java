package com.example.text.java;

import org.springframework.util.StopWatch;
import org.testng.annotations.Test;

/**
 * java计时器
 * 线程不安全，多线程使用---Can't start StopWatch: it's already running
 * @author yuez
 * @since 2022/7/5
 */
public class StopWatchTest {

    @Test
    public void First() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("task1");
        Thread.sleep(1000*3);
        System.out.println("now task:"+stopWatch.currentTaskName());
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        stopWatch.start("task2");
        Thread.sleep(1000*2);
        System.out.println("now task:"+stopWatch.currentTaskName());
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.shortSummary());
        System.out.println(stopWatch.currentTaskName());
        System.out.println(stopWatch.getLastTaskName());
        System.out.println(stopWatch.getLastTaskInfo());

        System.out.println("************************");
        System.out.println("总耗时："+stopWatch.getTotalTimeMillis());
        System.out.println("任务数："+stopWatch.getTaskCount());
        System.out.println("所有任务详情："+stopWatch.getTaskInfo());

    }

}
