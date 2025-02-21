package com.boot.util.wiscom.pool;

import com.boot.util.wiscom.GlobalObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.*;

/**
 * 固定线程池
 * @author hwang
 */
@Log4j2
public class FixedThreadPool {

    /**公用线程池*/
    private volatile static ExecutorService publicThreadPool = null;

    /**自动根据任务数和cpu线程数确定线程池参数*/
    public static ExecutorService getFixedThreadPool(String taskName,int contentSize) {
        ThreadFactory namedThreadFactory =new ThreadFactoryBuilder().setNameFormat("lddd-batch-%d").build();
        //根据服务器cpu线程数确定线程池参数
        int processorSize = Runtime.getRuntime().availableProcessors();
        int corePoolSize = contentSize;
        int maxPoolSize = contentSize;
        int queueSize = 1;
        if (processorSize > contentSize) {
            maxPoolSize = processorSize;
            queueSize = processorSize - contentSize;
        } else if(processorSize < contentSize) {
            corePoolSize = processorSize;
            queueSize = contentSize - processorSize;
        }
       return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**手动设置线程池参数*/
    public static ExecutorService getFixedThreadPool(String taskName,int corePoolSize,int maxPoolSize,int queueSize,long keepAliveTime) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(taskName + "-%d").build();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
    /**获取线程池*/
    public static ThreadPoolExecutor getNewThreadPool(String taskName,int corePoolSize,int maxPoolSize,int queueSize,long keepAliveTime) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(taskName + "-%d").build();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**获取一个公用的线程池*/
    public static ExecutorService getPublicThreadPool() {
        if (publicThreadPool == null) {
            publicThreadPool = getFixedThreadPool("publicThread",
                    Integer.parseInt(GlobalObject.properties_business_my.getProperty("threadPool.corePoolSize")),
                    Integer.parseInt(GlobalObject.properties_business_my.getProperty("threadPool.maxPoolSize")),
                    Integer.parseInt(GlobalObject.properties_business_my.getProperty("threadPool.queueSize")),
                    Long.valueOf(GlobalObject.properties_business_my.getProperty("threadPool.keepaliveTime")));
        }
        return publicThreadPool;
    }
    /**
     * 等待多线程任务结束
     * @param fixedThreadPool 线程池
     * @param taskName 任务名称
     * @param timeStart 任务开始时间 System.currentTimeMillis()
     * @param timeOut 任务超时时间 单位：秒
     * @return boolean true:任务按时完成 false:任务超时或出错
     */
    public static boolean waitForTasksByTime(ExecutorService fixedThreadPool, String taskName, long timeStart, int timeOut) {
        try {
            fixedThreadPool.shutdown();
            if (fixedThreadPool.awaitTermination(timeOut,TimeUnit.SECONDS)) {
                log.info("{}任务全部完成，耗时{}毫秒", taskName,System.currentTimeMillis() - timeStart);
                return true;
            } else {
                log.warn("{}任务{}秒超时",taskName,timeOut);
                fixedThreadPool.shutdownNow();
                return false;
            }
        } catch (InterruptedException ex) {
            log.error("{}任务结束等待错误，原因:{}",taskName,ex.getMessage());
            fixedThreadPool.shutdownNow();
            return false;
        }
    }
    /**
     * 等待多线程任务结束
     * @param fixedThreadPool 线程池
     * @param taskName 任务名称
     * @param timeStart 任务开始时间 System.currentTimeMillis()
     * @param timeOut 任务超时时间 单位：秒
     * @return boolean true:任务按时完成 false:任务超时或出错
     */
    public static boolean waitForTasksNotStop(ThreadPoolExecutor fixedThreadPool, String taskName, long timeStart, int timeOut) {
        boolean retFlag = false;
        fixedThreadPool.shutdown();
        while (true) {
            try {
                if (fixedThreadPool.awaitTermination(timeOut,TimeUnit.SECONDS)) {
                    log.info("{}任务全部完成，耗时{}毫秒", taskName,System.currentTimeMillis() - timeStart);
                    retFlag = true;
                    break;
                } else {
                    log.info("{}任务:核心线程数{} 活跃任务数{} 等待队列数{} 已耗时{}毫秒",taskName,
                            fixedThreadPool.getCorePoolSize(),fixedThreadPool.getActiveCount(),fixedThreadPool.getQueue().size(),
                            System.currentTimeMillis() - timeStart);
                }
            } catch (InterruptedException ex) {
                log.error("{}任务结束等待错误，原因:{}",taskName,ex.getMessage());
                fixedThreadPool.shutdownNow();
                break;
            }
        }
        return retFlag;
    }
    /**
     * 等待公用线程池任务返回
     * @param taskName 任务名称
     * @param timeStart 任务开始时间 System.currentTimeMillis()
     * @param timeOut 任务超时时间 单位：秒
     * @param listFuture 线程返回的运行时间（单位：毫秒）列表
     */
    public static void waitForPublicTasksByTime(String taskName,long timeStart, int timeOut, List<Future<Long>> listFuture) {
        //计算线程返回的时间的累加值
        long sumTime = listFuture.stream().reduce(0L, (sum, y) -> {
            try {
                sum += y.get(timeOut,TimeUnit.SECONDS);
                return sum;
            } catch (Exception ex) {
                return -1L;
            }
        }, Long::sum);
        if (-1L == sumTime) {
            log.info("{}任务全部完成，耗时{}毫秒,线程错误返回", taskName,
                    System.currentTimeMillis() - timeStart);
        } else {
            log.info("{}任务全部完成，耗时{}毫秒,线程累加时间{}毫秒", taskName,
                    System.currentTimeMillis() - timeStart, sumTime);
        }
    }
}
