package com.example.text.demoOnLine.设计模式.行为型;

/**
 * 状态模式
 * 行为设计模式，
 * 它允许对象在内部状态改变时改变它的行为。状态模式通过将状态封装成一个对象来实现这一点，从而使得一个对象的行为取决于它的状态对象，而不是取决于对象本身。
 * state 是一个接口，封装了状态及其行为
 * ConcreteState X 具体实现
 * Context 保持并切换各个状态，其持有一个State的引用。它将依赖状态的各种操作委托给不同的状态对象执行。其负责与客户端交互
 *  优点：
 *  增强了程序的可扩展性，因为我们很容易添加一个State
 * 增强了程序的封装性，每个状态的操作都被封装到了一个状态类中
 * 缺点：
 * 类变多了
 *
 * 策略模式定义了一组可互相代替的算法，这一组算法对象完成的是同一个任务，只是使用的方式不同，例如同样是亿万富翁，马云通过卖东西实现，而王思聪通过继承实现。
 * 状态模式不同的状态完成的任务完全不一样。
 * @author yuez
 * @since 2023/4/21
 */
public class 状态模式 {
    public static void main(String[] args) {
        Order order = new Order();
        order.processOrder();
        order.processOrder();
        order.processOrder();
    }

}
interface OrderState{
    void processOrder(Order context);
}

//必须要有一个Context类，这个类持有State接口，负责保持并切换当前的状态
// 属性可以向下传递状态，自动切换状态
class Order{
    private OrderState orderState;

    public Order(OrderState orderState) {
        this.orderState = orderState;
    }

    public Order() {
        orderState = new NewOrder();
    }

    public void setState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void processOrder(){
        orderState.processOrder(this);
    }
}
//具体的状态实现
class NewOrder implements OrderState{

    @Override
    public void processOrder(Order context) {
        System.out.println("Processing new order.");
        context.setState(new ProcessingOrder());
    }
}

class ProcessingOrder implements OrderState{

    @Override
    public void processOrder(Order context) {
        // 处理处理中状态下的订单
        System.out.println("Processing order in progress.");
        context.setState(new CompletedOrder());
    }
}

class CompletedOrder implements OrderState{

    @Override
    public void processOrder(Order context) {
        // 处理完成状态下的订单
        System.out.println("Processing completed order.");
    }
}