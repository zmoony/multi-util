package com.example;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Task
 * 通过@DubboReference 从 Dubbo 获取了一个 RPC 订阅
 * @author yuez
 * @since 2023/3/30
 */
@Component
public class Task implements CommandLineRunner {

    @DubboReference(group = "group1",version = "1.0")
    private IDemoService demoService;

    @Override
    public void run(String... args) throws Exception {
        String result = demoService.sayHello("aurora");
        System.out.println("receive result =====>" + result);

        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(new Date() + " Receive result ======> " + demoService.sayHello("world"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
