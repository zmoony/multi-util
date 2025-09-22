package com.example.text.demoOnLine.设计模式.创建型;

/**
 * 单例模式
 *
 * @author yuez
 * @since 2023/4/18
 */
public class 单例模式 {
//    懒汉模式 {@link  Singleton}
//    静态内部类 {@link SingletonStatic}
//    饿汉式 {@link SingletonHungry}
/*
    线程安全：饿汉+双重锁的懒汉
    第一次访问创建：懒汉+静态内部类
    代码少，不考虑安全：枚举
*/

}

/**
 * 懒汉模式
 * 注意:有线程安全问题，需要加双重锁
 */
class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

/**
 * 饿汉式
 */
class SingletonHungry {
    private static SingletonHungry instance = new SingletonHungry();

    private SingletonHungry() {
    }

    public static SingletonHungry getInstance() {
        return instance;
    }
}

/**
 * 静态内部类
 * SingletonHolder是一个静态内部类，它包含一个静态的INSTANCE成员变量，
 * 用于存储单例对象。在第一次调用getInstance方法时，静态内部类会被加载，从而创建单例对象。这种方式既兼顾了线程安全又兼顾了延迟加载的需求。
 */
class SingletonStatic {
    private SingletonStatic() {
    }

    public static SingletonStatic getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 1. 当程序运行并且首次尝试通过 SingletonStatic.getInstance() 获取单例实例时，JVM 才开始加载 SingletonHolder 类。
     * 2. 在加载 SingletonHolder 类的过程中，它会执行静态初始化块（在这个例子中是静态final字段 INSTANCE = new SingletonStatic(); 的初始化）。
     * 3. 这个初始化过程是线程安全的，因为JVM保证了类的初始化过程在同一时间只允许一个线程执行。
     * 4. 一旦 SingletonHolder.INSTANCE 被初始化，后续的所有 SingletonStatic.getInstance() 调用都将直接返回这个已经初始化好的单例实例，而不需要再次进行初始化，从而确保了实例的唯一性和线程安全性。
     */
    private static class SingletonHolder {
        //JVM的类加载是线程安全的
        private static final SingletonStatic INSTANCE = new SingletonStatic();
    }
}

enum Sington{
    SINGTON;

    public void doSomething(){}
}




