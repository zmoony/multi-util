package com.boot.websocket.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ValTestController
 * 存在成员变量的编辑--有状态的bean
 *  不存在成员变量或者只存在成员变量的查看--无状态的bean
 * @author yuez
 * @since 2024/11/14
 */
@RestController
@Log4j2
public class ValTest2Controller {

    /**
     * 使用threadLocal 解决线程安全。注意使用完需要remove一下，防止内存泄露
     *  正常情况下，每次请求都是独立的，第一次是1
     *  但是当线程池复用线程时，会复用线程，导致count值会累加，所以需要结束请求时清空。一般可采用过滤器的形式
     */
    private ThreadLocal<Integer> count = ThreadLocal.withInitial(()->0);

    @PostMapping(value = "/thread/add")
    public void add() {
        count.set(count.get()+1);
        log.info("count:{}",count.get());
    }

    /**
     * 使用锁机制
     * 单例的时候，成员共享。或者直接使用原型模式，每次创建新的对象，
     */
    private Integer num = 0;
    private AtomicInteger count2 = new AtomicInteger(0);
    @PostMapping(value = "/lock/add")
    public String add2() {
        synchronized (this){
            num++;
        }
        log.info("num:{}",num);
        return "success";
    }

    @PostMapping(value = "/atomic/add")
    public void add3() {
        count2.getAndIncrement();
        log.info("count2:{}",count2.get());
    }

}
