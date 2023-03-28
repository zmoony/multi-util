package com.example.text.queue;

import org.testng.annotations.Test;

import javax.validation.constraints.NotNull;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 需求：延时发送请求（晚上发的消息，次日早上发送）
 *
 * 在原生的Java有DelayQueue供我们去使用，在使用的时候，
 * 我们add进去的队列的元素需要实现Delayed接口（同时该接口继承了Comparable接口，所以我们DelayQueue是有序的）
 *
 *
 * PriorityQueue：优先队列，基于优先堆的无界队列，这个优先队列中的元素可以默认自然排序或者通过提供的Comparator（比较器）在队列实例化的时排序。
 * 优先队列不允许空值，而且不支持non-comparable（不可比较）的对象，比如用户自定义的类。优先队列要求使用Java Comparable和Comparator接口给对象排序，并且在排序时会按照优先级处理其中的元素。
 * 优先队列的头是基于自然排序或者Comparator排序的最小元素+大小是不受限制的，但在创建时可以指定初始大小+priorityQueue是非线程安全的，所以Java提供了PriorityBlockingQueue
 *
 *
 * @author yuez
 * @since 2022/6/16
 */
public class DelayqueueTest {

    /**
     * DelayQueue = BlockingQueue + PriorityQueue + Delayed
     * DelayQueue是一个无界的BlockingQueue，用于放置实现了Delayed接口的对象，其中的对象只能在其到期时才能从队列中取走。
     * 这种队列是有序的，即队头对象的延迟到期时间最长。注意：不能将null元素放置到这种队列中。
     * <br><br>
     * 缺点：
     * <pre>1.不能持久化，重启会丢失数据</pre>
     * <pre>2.不支持集群模式</pre>
     *
     * 代替方案：
     * redis和消息队列（kafka/rocketMQ/rabbmitmq）
     * <ul>
     * <li>redis:zset+过期回调</li>
     * 使用数据结构：zset（有序，可持久化）实例：有赞延迟队列（https://tech.youzan.com/queuing_delay/）利用redis的过期回调机制
     * <li>kafka:</li>
     * <li>rabbmitmq:ttl+死信队列</li>
     * 通过ttl,触发过期时，送到Dead Letter Exchanges（死信队列中)，将此队列的元素再次转发进行消费，达到延迟机制
     * <li>rocketMQ：</li>
     * 支持在我们投递消息的时候设置延迟等级(默认18等级对应的时间：messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h)
     * {@code
     *  Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes());
     * // This message will be delivered to consumer 10 seconds later.
     * message.setDelayTimeLevel(3);
     * // Send the message
     * producer.send(message);
     * }
     * 当我们设置了延迟等级的消息之后，RocketMQ不会把消息直接投递到对应的topic，而是转发到对应延迟等级的队列中。
     * 在Broker内部会为每个延迟队列起TimerTask来进行判断是否有消息到达了时间。
     * </ul>
     *
     * 实现方案：
     * <b>万物皆扫表</b>
     * 针对这次需求（晚上发的消息，次日早上发送），就不需要上延时队列，因为austin已经接入了分布式定时任务框架了（对应的实现是xxl-job）
     * 只要把晚上的接收到的消息扔进Redis list，然后启个定时任务（每天早上9点）轮询该list是否有数据，如果有再重新做处理就完事了。
     */
    @Test
    public void DelayQueueJdk() throws InterruptedException {
        DelayQueue delayQueue = new DelayQueue();
        new Thread(()->{
            delayQueue.offer(new MyDelayTask("task1",10000));
            delayQueue.offer(new MyDelayTask("task2",3900));
            delayQueue.offer(new MyDelayTask("task3",1900));
            delayQueue.offer(new MyDelayTask("task4",5900));
            delayQueue.offer(new MyDelayTask("task5",6900));
            delayQueue.offer(new MyDelayTask("task6",7900));
            delayQueue.offer(new MyDelayTask("task7",4900));

        }).start();
        while (true){
            Delayed take = delayQueue.take();
            System.out.println(take);
        }

    }
}
class MyDelayTask implements Delayed{
    private String name;
    private Long start = System.currentTimeMillis();
    private long time;

    public MyDelayTask(String name, long time) {
        this.name = name;
        this.time = time;
    }

    /**
     * 获取延迟时间 过期时间-当前时间
     * @param unit the time unit 为0的时间取出
     * @return
     */
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return unit.convert((start+time) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(@NotNull Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MyDelayTask{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
