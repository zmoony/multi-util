package com.example.text.demoOnLine.时间订单30分钟未支付自动取消;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Tuple;

import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 利用 redis 的 zset,zset 是一个有序集合，每一个元素(member)都关联了一个 score,通过 score 排序来取集合中的值
 * 添加元素:ZADD key score member [score member …]
 * 按顺序查询元素:ZRANGE key start stop [WITHSCORES]
 * 查询元素 score:ZSCORE key member
 * 移除元素:ZREM key member [member …]
 * <code>
 * 添加单个元素
 * redis> ZADD page_rank 10 google.com
 * (integer) 1
 * <p>
 * 添加多个元素
 * redis> ZADD page_rank 9 baidu.com 8 bing.com
 * (integer) 2
 * <p>
 * redis> ZRANGE page_rank 0 -1 WITHSCORES
 * 1) "bing.com"
 * 2) "8"
 * 3) "baidu.com"
 * 4) "9"
 * 5) "google.com"
 * 6) "10"
 * <p>
 * 查询元素的score值
 * redis> ZSCORE page_rank bing.com
 * "8"
 * <p>
 * 移除单个元素
 * redis> ZREM page_rank google.com
 * (integer) 1
 * <p>
 * redis> ZRANGE page_rank 0 -1 WITHSCORES
 * 1) "bing.com"
 * 2) "8"
 * 3) "baidu.com"
 * 4) "9"
 * </code>
 *
 * <code>
 *     方案二
 *     利用失效的回调机制
 * </code>
 *
 * @author yuez
 * @since 2023/2/9
 */
public class redis缓存 {
    private static final String ADDR = "172.18.12.21";
    private static final int PORT = 6379;
    private static final String AUTH = "wiscom123!";
    private static JedisPool jedisPool = new JedisPool(ADDR, PORT);
    private static RedisSub sub = new RedisSub();

    //Keyspace Notifications ---不可靠
    public static void init(){
        new Thread(()->{
            getJedis().subscribe(sub,"__keyevent@0__:expired");
        }).start();
    }

    static class RedisSub extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            System.out.println("System.currentTimeMillis() + \"ms:\" + message + \"订单取消\"");
        }
    }

    public static Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        jedis.auth(AUTH);
        return jedis;
    }

    public void productionDelayMessage() {
        for (int i = 0; i < 5; i++) {
            //延迟3秒
            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.SECOND, 3);
            int second3later = (int) (cal1.getTimeInMillis() / 1000);
            redis缓存.getJedis().zadd("OrderId", second3later, "OID0000001" + i);
            System.out.println(System.currentTimeMillis() + "ms:redis生成了一个订单任务：订单ID为" + "OID0000001" + i);
        }
    }

    /**
     * 查询-删除  要保证原子性，否则并发会有问题
     * @throws InterruptedException
     */
    public void consumerDelayMessage() throws InterruptedException {
        Jedis jedis = redis缓存.getJedis();
        while (true) {
            Set<Tuple> items = jedis.zrangeWithScores("OrderId", 0, 1);
            if (items == null || items.isEmpty()) {
                System.out.println("当前没有等待任务");
                Thread.sleep(500);
                continue;
            }
            int score = (int) ((Tuple) items.toArray()[0]).getScore();
            Calendar cal = Calendar.getInstance();
            int nowSecond = (int) (cal.getTimeInMillis() / 1000);
            if (nowSecond >= score) {
                String orderId = ((Tuple) items.toArray()[0]).getElement();
                //判断删除成功了才能消费
                Long num = jedis.zrem("OrderId", orderId);
                if(num != null && num >0 ){
                    System.out.println(System.currentTimeMillis() + "ms:redis消费了一个任务：消费的订单OrderId为" + orderId);
                }
            }
        }
    }

    public void threadTestZset(){
        redis缓存 redis缓存 = new redis缓存();
        redis缓存.productionDelayMessage();
//        redis缓存.consumerDelayMessage();
        //并发测试
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 5, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                new ThreadFactoryBuilder().setNameFormat("redis-demo-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 5; i++) {
            CompletableFuture.supplyAsync(() -> {
                try {
                    new redis缓存().consumerDelayMessage();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return 0;
            }, pool);
        }
    }

    public void keyNotification(){
        //失效检测
        init();
        for (int i = 0; i < 10; i++) {
            String orderId = "OID000000" + i;
            getJedis().setex(orderId,3,orderId);
            System.out.println(System.currentTimeMillis() + "ms:" + orderId + "订单生成");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new redis缓存().keyNotification();
    }


}
//并发测试
