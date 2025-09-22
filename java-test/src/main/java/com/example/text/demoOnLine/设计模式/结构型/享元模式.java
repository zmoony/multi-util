package com.example.text.demoOnLine.设计模式.结构型;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元模式
 * 结构型设计模式
 * 通过共享对象来减少内存占用和提高系统性能。享元模式适用于大量细粒度的对象，这些对象具有相似的属性，而且这些属性可以被共享
 *
 * 在享元模式中，我们将对象分为两种：
 * 内部状态指对象共享的部分，可以被多个对象共享；
 * 外部状态则指对象独有的部分，不能被共享。
 *
 * 通过将内部状态抽取出来，我们可以减少系统中需要创建的对象数量，从而降低内存消耗。
 *
 * （1）享元工厂：用于创建具体享元类，维护相同的享元对象。当请求对象已经存在时，直接返回对象，不存在时，在创建对象。在例子中的解释就是图书馆，保存了所有的书，当学生借书时，有就拿走，没有买一本新书。这里面其实是使用了单例模式的。
 *
 * （2）抽象享元：定义需要共享的对象业务接口。享元类被创建出来总是为了实现某些特定的业务逻辑.
 *
 * （3）具体享元：实现抽象享元类的接口，完成某一具体逻辑。在这里表示可以被借出。
 *
 * @author yuez
 * @since 2023/4/25
 */
public class 享元模式 {
    public static void main(String[] args) {
        String[] strings = {"hello", "world", "hello", "java", "world"};
        for (String string : strings) {
            Flyweight flyweight = FlyweightFactory.getFlyweight(string);
            flyweight.print(string);
        }
        System.out.println("Flyweight count: " + FlyweightFactory.flyweights.size());
        for (Flyweight flyweight : FlyweightFactory.flyweights.values()) {
            System.out.println("String: " + ((ConcreteFlyweight) flyweight).value +
                    ", Count: " + ((ConcreteFlyweight) flyweight).getCount());
        }
    }
}
//抽象享元
interface Flyweight{
    void print(String str);
}
//定义具体享元类
class ConcreteFlyweight implements Flyweight{
    String value;
    int count;

    public ConcreteFlyweight(String value) {
        this.value = value;
        this.count = 0;
    }

    @Override
    public void print(String str) {
        count++;
    }

    public int getCount(){
        return count;
    }
}

/**
 * 享元工厂（Llibrary）
 * 使用 FlyweightFactory 来管理共享的字符串对象。每个具体的 Flyweight 实现类都包含一个字符串值和一个计数器，
 * 用于记录该字符串的使用次数。当 Client 类使用 FlyweightFactory 来存储字符串时，它将创建或获取一个共享的 Flyweight 对象，
 * 并使用它来存储字符串。最后，我们可以查看 FlyweightFactory 中保存的所有 Flyweight 对象，以及它们的使用次数。
 */
class FlyweightFactory{
    public static final Map<String,Flyweight> flyweights = new HashMap<>();

    public static Flyweight getFlyweight(String value) {
        return flyweights.computeIfAbsent(value,key-> new ConcreteFlyweight(value));
    }
}