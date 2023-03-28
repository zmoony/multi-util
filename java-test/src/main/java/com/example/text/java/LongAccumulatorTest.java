package com.example.text.java;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder和LongAccumulator
 * LongAdder 更快更安全
 * LongAccumulator 可以自定义步阶
 * @author yuez
 * @since 2023/3/17
 */
public class LongAccumulatorTest {
    volatile int i = 0;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    LongAdder longAdder = new LongAdder();
    LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 0);

    public synchronized void sync_add() {
        i++;
    }

    public void sync_in_add() {
        synchronized (this) {
            i++;
        }
    }

    public void atomic() {
        atomicInteger.incrementAndGet();
    }

    public void add() {
        longAdder.increment();
    }

    public void add_LongAccumulator() {
        longAccumulator.accumulate(1);
    }

    public static void main(String[] args) {
        LongAccumulatorTest demo = new LongAccumulatorTest();
        int size_thread = 50;
        int _1W = 10000;
        long startTime;
        long endTime;
        ExecutorService pool = Executors.newFixedThreadPool(size_thread);

        startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < _1W; i++) {
                demo.sync_add();
            }
        }, pool).join();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t syc_add()"+"\t"+demo.i);

        startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < _1W; i++) {
                demo.sync_in_add();
            }
        }, pool).join();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t sync_in_add()"+"\t"+demo.i);

        startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < _1W; i++) {
                demo.atomic();
            }
        }, pool).join();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t atomic()"+"\t"+demo.atomicInteger.get());

        startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < _1W; i++) {
                demo.add_LongAccumulator();
            }
        }, pool).join();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t add_LongAccumulator()"+"\t"+demo.longAccumulator.get());

        startTime = System.currentTimeMillis();
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < _1W; i++) {
                demo.add();
            }
        }, pool).join();
        endTime = System.currentTimeMillis();
        System.out.println("----costTime: "+(endTime - startTime) +" 毫秒"+"\t add()"+"\t"+demo.longAdder.longValue());

        pool.shutdown();

    }

}
