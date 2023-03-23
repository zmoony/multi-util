package com.example.text.demoOnLine.时间订单30分钟未支付自动取消;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * HashedWheelTimer
 * 优点：效率高,任务触发时间延迟时间比 delayQueue 低，代码复杂度比 delayQueue 低。
 * 缺点：服务器重启后，数据全部消失，怕宕机；集群扩展相当麻烦；因为内存条件限制的原因，比如下单未付款的订单数太多，那么很容易就出现 OOM 异常
 * @author yuez
 * @since 2023/2/9
 */
public class 时间轮算法 {
    static class MyTimerTask implements TimerTask{
        boolean flag;

        public MyTimerTask(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            System.out.println("要去数据库删除订单了。。。。");
            this.flag = false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyTimerTask myTimerTask = new MyTimerTask(true);
        Timer timer = new HashedWheelTimer();
        timer.newTimeout(myTimerTask,5, TimeUnit.SECONDS);
        int i =1;
        while (myTimerTask.flag){
            Thread.sleep(1000);
            System.out.println(i + "秒过去了");
            i++;
        }
    }



}
