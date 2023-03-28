package com.example.text.disruptor.event;


import com.lmax.disruptor.EventHandler;

//定义事件消费者
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("消费者:"+event.getValue());
    }
}
