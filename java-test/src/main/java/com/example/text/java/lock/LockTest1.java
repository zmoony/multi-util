package com.example.text.java.lock;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.*;
import java.util.function.Consumer;

/**
 * LockTest1
 * 需求：开200个线程。每个线程里面for循环累加0-100w，累加的那步用个全局锁锁起来
 * 分别用，读锁，写锁，正常lock
 *
 *
 *
 * @author yuez
 * @since 2024/2/19
 */
public class LockTest1 {


    int threadNum = 200;
    int startNum = 0;
    int endNum = 1000000;

    @Test
    public void testStaticLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        for (int i = 0; i < threadNum; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    synchronized (LockTest1.class) {
                        num.add(j);
                    }
                }
            });
            future.join();
        }
        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void testCommonLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        for (int i = 0; i < threadNum; i++) {
            LockTest1 lockTest1 = new LockTest1();
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    synchronized (lockTest1) {
                        num.add(j);
                    }
                }
            });
            future.join();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        final Lock lock = new ReentrantLock();
        for (int i = 0; i < threadNum; i++) {
            CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(j);
                    } finally {
                        lock.unlock();
                    }

                }
            }).join();
        }
        System.out.println(num);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLock2() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        final Lock lock = new ReentrantLock();
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(j);
                    } finally {
                        lock.unlock();

                    }
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(num);
        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void testReadLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.readLock();
        for (int i = 0; i < threadNum; i++) {
            CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(j);
                    } finally {
                        lock.unlock();
                    }

                }
            }).join();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testReadLock2() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.readLock();
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(j);
                    } finally {
                        lock.unlock();
                    }

                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 性能最优
     */
    @Test
    public void testReadLock3() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        StampedLock readWriteLock = new StampedLock();
        List<CompletableFuture<Void>> list = new ArrayList<>(200);
        for (int i = 0; i < threadNum; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    if (readWriteLock.validate(readWriteLock.tryOptimisticRead())) {
                        num.add(j);
                    } else {
                        long l = readWriteLock.readLock();
                        try {
                            num.add(j);
                        } finally {
                            readWriteLock.unlockRead(l);
                        }
                    }
                }
            });
            list.add(future);
        }
        list.forEach(CompletableFuture::join);
       /* aaaa(this::testReadLock3);

        aaaa((s) -> this.accept(s),"wiscom");*/
        System.out.println(System.currentTimeMillis() - start);
    }



    public void accept(String s) {
        s.getBytes();
    }

    public void aaaa(Runnable runnable) {
        runnable.run();
    }

    public void aaaa(Consumer<String> consumer,String aaaaaaaa) {
        consumer.accept(aaaaaaaa);
//        new ArrayList<>().stream().r
    }

    @Test
    public void testWriteLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.writeLock();
        List<CompletableFuture<Void>> list = new ArrayList<>(200);
        for (int i = 0; i < threadNum; i++) {
            long aaaa = System.currentTimeMillis();
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(j);
                    } finally {
                        lock.unlock();
                    }

                }
//                try {
//                    Thread.sleep(5000);
//                } catch (Exception ignore) {
//                }
            });
            list.add(future);

        }
        list.forEach(CompletableFuture::join);
        System.out.println(System.currentTimeMillis() - start);
    }


}
