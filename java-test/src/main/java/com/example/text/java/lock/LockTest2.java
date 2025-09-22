package com.example.text.java.lock;

import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LockTest1
 * 需求：开200个线程。每个线程里面for循环累加0-100w，累加的那步用个全局锁锁起来
 * 分别用，读锁，写锁，正常lock
 * @author yuez
 * @since 2024/2/19
 */
public class LockTest2 {

    
    int threadNum = 200;
    int startNum = 0;
    int endNum = 1000000;

    @Test
    public void testStaticLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                for (int j = startNum; j < endNum; j++) {
                    synchronized (LockTest2.class) {
                        num.add(1);
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void testCommonLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            LockTest2 lockTest1 = new LockTest2();
            new Thread(() -> {
                for (int j = startNum; j < endNum; j++) {
                    synchronized (lockTest1) {
                        num.add(1);
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        final Lock lock = new ReentrantLock();

        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            LockTest2 lockTest1 = new LockTest2();
            new Thread(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(1);
                    } finally {
                        lock.unlock();
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void testReadLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.readLock();
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            LockTest2 lockTest1 = new LockTest2();
            new Thread(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(1);
                    } finally {
                        lock.unlock();
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testWriteLock() {
        long start = System.currentTimeMillis();
        LongAdder num = new LongAdder();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.writeLock();
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            LockTest2 lockTest1 = new LockTest2();
            new Thread(() -> {
                for (int j = startNum; j < endNum; j++) {
                    lock.lock();
                    try {
                        num.add(1);
                    } finally {
                        lock.unlock();
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(System.currentTimeMillis() - start);
    }


}
