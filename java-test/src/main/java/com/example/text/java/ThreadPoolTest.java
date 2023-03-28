package com.example.text.java;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.util.StopWatch;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

/**
 * 参考：https://juejin.cn/post/7124124854747398175
 * 几种多线程的测试
 * future get等待返回
 * futuretask 多了runable方法
 * complateservice 优先返回先计算完的
 * complateFuture 知道是哪个先完成的，先返回
 *
 * @author yuez
 * @since 2022/7/18
 */
public class ThreadPoolTest {

    /**
     * 顺序返回，必须等第一个结束才能往下获取返回值，其他的计算计算技术也要等方法一接收后一起返回，
     * future.get(5, TimeUnit.SECONDS);方法阻塞
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    public void FutureTest() throws ExecutionException, InterruptedException, TimeoutException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        Future<String> future1 = pool.submit(() -> {
            sleep(4000);
            return "call-1";
        });
        Future<String> future2 = pool.submit(() -> {
            sleep(1000);
            return "call-2";
        });
        Future<String> future3 = pool.submit(() -> {
            sleep(3000);
            return "call-3";
        });
        List<Future<String>> futures = Lists.newArrayList(future1, future2, future3);
        for (Future<String> future : futures) {
            String s = future.get(5, TimeUnit.SECONDS);
            System.out.println(s);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + stopWatch.getTaskInfo());
    }

    /**
     * 提供Future的基础实现，并实现了Runnable接口。
     * 包含了取消与启动计算的方法，查询计算是否完成以及检索计算结果的方法。只有在计算完成才能检索到结果，
     * 调用get()方法时如果任务还没有完成将会阻塞调用线程至到任务完成。一旦计算完成就不能重新开始与取消计算，但可以调用runAndReset()重置状态后再重新计算。
     * 适配 ：execute没有返回值的情况
     * 对比Future:实现了runable的接口，可以直接交给execute
     */
    @Test
    public void FutureTaskTest() throws ExecutionException, InterruptedException, TimeoutException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        FutureTask<String> future1 = new FutureTask<>(() -> {
            sleep(4000);
            return "call-1";
        });
        FutureTask<String> future2 = new FutureTask<>(() -> {
            sleep(1000);
            return "call-2";
        });
        FutureTask<String> future3 = new FutureTask<>(() -> {
            sleep(3000);
            return "call-3";
        });
        pool.execute(future1);
        pool.execute(future2);
        pool.execute(future3);
        List<Future<String>> futures = Lists.newArrayList(future1, future2, future3);
        for (Future<String> future : futures) {
            String s = future.get(5, TimeUnit.SECONDS);
            System.out.println(s);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + stopWatch.getTaskInfo());
    }

    /**
     * 任务谁先完成返回谁的
     * 实现类：ExecutorCompletionService
     * 适合：只需返回一个即可的场景，优先返回第一个返回的
     */
    @Test
    public void ComplateServiceTest() throws InterruptedException, ExecutionException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        ExecutorCompletionService completionService = new ExecutorCompletionService(executor);
        completionService.submit(() -> {
            sleep(4000);
            return "call-1";
        });
        completionService.submit(() -> {
            sleep(1000);
            return "call-2";
        });
        completionService.submit(() -> {
            sleep(2000);
            return "call-3";
        });
        for (int i = 0; i < 3; i++) {
            //take 阻塞方法
            System.out.println(completionService.take().get());
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + stopWatch.getTaskInfo().toString());
    }

    /**
     * 实现了Future接口和CompletionStage接口
     * CompleteFuture知道当前完成的是谁，并采用编程式回调提高代码可读性
     * 注意：初始化的时候要指定Executor，否则使用的是默认系统及公用线程池ForkJoinPool,为守护线程
     * 其中supplyAsync用于有返回值的任务，runAsync则用于没有返回值的任务。Executor参数可以手动指定线程池，否则默认ForkJoinPool.commonPool()系统级公共线程池，
     * 注意：这些线程都是Daemon线程，主线程结束Daemon线程不结束，只有JVM关闭时，生命周期终止。
     * 可以顺序执行；也可以根据任一返回值直接运行下一步
     */
    @Test
    public void ComplateFutureTest() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        ExecutorService pool = Executors.newFixedThreadPool(5);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 5, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new ThreadFactoryBuilder().setNameFormat("redis-demo-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "call-1";
        }, pool);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "call-2";
        }, pool);
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "call-3";
        }, pool);

        String collect = Stream.of(future1, future2, future3).map(CompletableFuture::join).collect(Collectors.joining(" "));
        stopWatch.stop();
        System.out.println(collect);
        System.out.println(stopWatch.prettyPrint());
        System.out.println("总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + stopWatch.getTaskInfo().toString());
    }


}
