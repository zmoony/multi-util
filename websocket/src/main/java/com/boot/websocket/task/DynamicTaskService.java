package com.boot.websocket.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * DynamicTaskService
 *
 * @author yuez
 * @since 2024/11/27
 */
@Service
public class DynamicTaskService {
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

    //内存占用比较高的时候，可以使用弱引用。这样当对象不在使用的时候，可以自动被垃圾会说
//    private WeakHashMap<String, ScheduledFuture<?>> scheduledTasks = new WeakHashMap<>();

    @Autowired
    public DynamicTaskService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * 添加动态任务
     * @param taskName  任务的名称，用于标识任务
     * @param taskRunnable  要执行的任务逻辑
     * @param interval  执行间隔（单位：毫秒）
     */
    public void addTask(String taskName, Runnable taskRunnable, long interval) {
        if (taskMap.containsKey(taskName)) {
            System.out.println("任务已存在：" + taskName);
            return;
        }
        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(taskRunnable, interval);
        taskMap.put(taskName, scheduledFuture);
        System.out.println("成功添加任务：" + taskName);
    }

    /**
     * 添加动态任务 使用cron表达式
     * @param taskName  任务的名称，用于标识任务
     * @param taskRunnable  要执行的任务逻辑
     * @param cronExpression  cron表达式
     */
    public void addTask(String taskName, Runnable taskRunnable,String cronExpression){
        if (taskMap.containsKey(taskName)) {
            System.out.println("任务已存在：" + taskName);
            return;
        }
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(taskRunnable, new CronTrigger(cronExpression));
        taskMap.put(taskName, scheduledFuture);
        System.out.println("成功添加任务：" + taskName);
    }



    /**
     * 更新动态任务
     * @param taskName  任务的名称
     * @param taskRunnable 要执行的任务逻辑
     * @param interval  执行间隔（单位：毫秒）
     */
    public void updateTask(String taskName, Runnable taskRunnable, long interval) {
        if (!taskMap.containsKey(taskName)) {
            System.out.println("任务不存在：" + taskName);
            return;
        }else{
            removeTask(taskName);
            addTask(taskName, taskRunnable, interval);
        }
    }



    /**
     * 删除动态任务
     * @param taskName 要删除的任务的名称
     */
    public void removeTask(String taskName) {
        ScheduledFuture<?> scheduledFuture = taskMap.remove(taskName);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);  // 取消任务
            System.out.println("成功删除任务：" + taskName);
        } else {
            System.out.println("任务不存在：" + taskName);
        }
    }

    /**
     * 获取所有动态任务
     * @return
     */
    public Map<String, ScheduledFuture<?>> getAllTasks() {
        return taskMap;
    }


}
