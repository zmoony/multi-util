package com.example.text.demoOnLine.设计模式.行为型;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 * 行为型模式；一对多，一个发生变化，通知其他依赖他的对象并自动给你更新
 * 1. 各类框架的监听机制
 * 2. 日志记录
 * 3. 线程通知
 *
 * @author yuez
 * @since 2023/4/18
 */
public class 观察者模式 {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        ConcreteObserver observer1 = new ConcreteObserver("observer1");
        ConcreteObserver observer2 = new ConcreteObserver("observer2");
        subject.registerObserver(observer1);
        subject.registerObserver(observer2);
        subject.notifyObservers("hello world");
    }
}

/**
 * 观察者接口
 */
interface Observer{
    void update(String message);
}

/**
 * 被观察者
 */
interface Subject{
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String message);
}

/**
 * 被观察者实现类
 */
class ConcreteSubject implements Subject{
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

class ConcreteObserver implements Observer{
    String name;

    public ConcreteObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name+" receive message :"+message);
    }
}