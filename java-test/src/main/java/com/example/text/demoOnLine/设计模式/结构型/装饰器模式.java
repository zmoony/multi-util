package com.example.text.demoOnLine.设计模式.结构型;

/**
 * 装饰器模式
 * 在不改变其结构的情况下，可以动态地扩展其功能。所有这些都可以釆用装饰器模式来实现(一个煎饼下 可以有不同的配料装饰)
 * 指在不改变现有对象结构的情况下，动态地给该对象增加一些职责（即增加其额外功能）的模式，它属于对象结构型模式。
 * 装饰器模式是一种结构型模式，它可以允许开发人员在不修改现有对象的情况下，动态地添加新功能。
 * 装饰器模式通过将一个对象包装在另一个对象中来扩展它的行为，从而提高了代码的灵活性和可重用性。
 *
 * 抽象构件（Component）角色：定义一个抽象接口以规范准备接收附加责任的对象。
 * 具体构件（ConcreteComponent）角色：实现抽象构件，通过装饰角色为其添加一些职责。
 * 抽象装饰（Decorator）角色：继承抽象构件，并包含具体构件的实例，可以通过其子类扩展具体构件的功能。
 * 具体装饰（ConcreteDecorator）角色：实现抽象装饰的相关方法，并给具体构件对象添加附加的责任。
 * @author yuez
 * @since 2023/4/18
 */
public class 装饰器模式 {

    public static void main(String[] args) {
        Component p = new ConcreteComponent();
        p.operation();
        System.out.println("--------");
        Decorator d = new ConcreteDecorator(p);
        d.operation();
    }

}
//抽象构建组件
interface Component {
    public void operation();
}
//具体组件实现
class ConcreteComponent implements Component{
    public ConcreteComponent() {
        System.out.println("创建具体构件角色");
    }
    @Override
    public void operation() {
        System.out.println("调用具体构件角色的方法operation()");
    }
}
//抽象装饰角色
abstract class Decorator implements Component {
    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        component.operation();
    }
}
//具体装饰角色
class ConcreteDecorator extends Decorator{

    public ConcreteDecorator(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        addedFunction();
    }

    private void addedFunction() {
        System.out.println("为具体构件角色增加额外的功能addedFunction()");
    }
}


