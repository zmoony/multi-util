package com.example.text.demoOnLine.设计模式.创建型;

/**
 * 工厂模式
 * 不直接实例化对象，封装一个通用接口和一组实现
 * 1. 创建对象复杂
 * 2. 动态修改某个对象
 * 3. 统一对象管理
 * 4. 根据不同的条件创建不同的对象
 *
 * @author yuez
 * @since 2023/4/18
 */
public class 工厂模式 {
    //调用
    public static void main(String[] args) {
        Product a = SimpleFactory.createProduct("A");
        a.operation();

        Product c = SimpleFactory.createProduct("C");
        c.operation();
    }
}
class SimpleFactory{
    public static Product createProduct(String type){
        if ("A".equals(type)){
            return new A();
        }else if ("B".equals(type)){
            return new B();
        }else{
            throw new IllegalArgumentException("invalid product type");
        }
    }
}

/**
 * 一个通用接口
 */
interface Product {
    void operation();
}

/**
 * 一组实现
 */
class A implements Product {

    @Override
    public void operation() {
        System.out.println("A。。。。。");
    }
}

class B implements Product{

    @Override
    public void operation() {
        System.out.println("B。。。。");
    }
}