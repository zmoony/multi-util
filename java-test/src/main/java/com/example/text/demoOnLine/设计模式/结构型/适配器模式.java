package com.example.text.demoOnLine.设计模式.结构型;

/**
 * 适配器模式
 * 将一个类的接口整合成客户端所需要的接口(可实现部分接口)
 * 1. 目标接口：客户端所需要的
 * 2. 适配器：整合接口（桥梁）
 * 3. 适配者：需要被适配的对象
 * 4. 客户端：使用目标接口的对象
 * @author yuez
 * @since 2023/4/18
 */
public class 适配器模式 {
    public static void main(String[] args) {
        Adaptee adaptee = new Adaptee();
        Target target = new Adapter(adaptee);
        target.request();
    }
}

/**
 * 目标接口
 */
interface Target{
    void request();
}

/**
 * 适配器
 */
class Adapter implements Target{
    private Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specialRequest();
    }
}

/**
 * 适配者
 */
class Adaptee{
    void specialRequest(){
        System.out.println("Adaptee specialRequest。");
    }
}

