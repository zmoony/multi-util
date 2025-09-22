package com.example.text.demoOnLine.设计模式.创建型;

/**
 * 原型模式
 * 创建型设计模式
 * 目的是通过复制现有对象来创建新的对象。
 *
 * @author yuez
 * @since 2023/4/24
 */
public class 原型模式 {
    public static void main(String[] args) {
        Prototype prototype = new ConcretePrototype();
        Prototype clone = prototype.clone();
    }
}
//原型接口
interface Prototype{
    public Prototype clone();
}
//具体实现
class ConcretePrototype implements Prototype{
    @Override
    public Prototype clone() {
        return new ConcretePrototype();
    }
}