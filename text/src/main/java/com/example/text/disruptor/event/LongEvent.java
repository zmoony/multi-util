package com.example.text.disruptor.event;

//定义事件event  通过Disruptor 进行交换的数据类型。
public class LongEvent {

    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
