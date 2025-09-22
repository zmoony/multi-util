package com.example.text.java;

import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.Md5Crypt;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * PhaserTest
 *
 * @author yuez
 * @since 2024/8/6
 */
public class PhaserTest {
    /**
     * 子线程会等全部子线程达到后才开始执行，实现类似CyclicBarrier的效果。
     */
    @Test
    public void test1() throws InterruptedException {
        List<Runnable> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            final int j = i;
            list.add(()-> System.out.println(j));
        }
        final Phaser phaser = new Phaser();
        int i =0;
        for (Runnable runnable : list){
            i++;
            final int j = i;
            phaser.register();
            new Thread(()->{
                try {
                    Thread.sleep(j * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //全部子线程到达后才开始执行
                phaser.arriveAndAwaitAdvance(); // await all creation
                runnable.run();
            }).start();
        }
        Thread.sleep(15000);
    }

    @Test
    public void test2() throws InterruptedException {
        System.out.println(Md5Crypt.apr1Crypt("admin"));
    }
}
