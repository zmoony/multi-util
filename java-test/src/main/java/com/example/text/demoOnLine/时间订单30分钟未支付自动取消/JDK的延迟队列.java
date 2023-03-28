package com.example.text.demoOnLine.时间订单30分钟未支付自动取消;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue object需实现Delayed 接口
 * 优点：效率高，低延迟
 * 缺点：不支持集群；宕机丢数据；内存要求高，会出现OOM;代码复杂度高
 * @author yuez
 * @since 2023/2/9
 */
public class JDK的延迟队列 implements Delayed {
    private String id;
    private long timeout;

    public JDK的延迟队列(String id, long timeout) {
        this.id = id;
        this.timeout = timeout+System.nanoTime();
    }
    //Delayed内部的排序依据
    @Override
    public int compareTo(Delayed o) {
        if(o == this){
            return 0;
        }
        JDK的延迟队列 t = (JDK的延迟队列)o;
        long d = getDelay(TimeUnit.NANOSECONDS) - t.getDelay(TimeUnit.NANOSECONDS);
        return d==0?0:(d<0?1:-1);
    }

    // 返回距离你自定义的超时时间还有多少
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeout-System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    void print() {
        System.out.println(id + "编号的订单要删除啦。。。。");
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<String>();
        list.add("00000001");
        list.add("00000002");
        list.add("00000003");
        list.add("00000004");
        list.add("00000005");
        DelayQueue<JDK的延迟队列> delayQueue = new DelayQueue<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            delayQueue.put(new JDK的延迟队列(list.get(i),TimeUnit.NANOSECONDS.convert(3,TimeUnit.SECONDS)));
        }
        System.out.println(delayQueue.size());
        while (true){
            delayQueue.take().print();
            System.out.println("After " + (System.currentTimeMillis() - start) + " MilliSeconds");
            System.out.println(delayQueue.size());
            if(delayQueue.size() == 0){
                break;
            }
        }

    }
}
