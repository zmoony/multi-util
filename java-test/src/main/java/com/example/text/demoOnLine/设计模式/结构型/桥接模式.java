package com.example.text.demoOnLine.设计模式.结构型;

/**
 * 桥接模式
 * 结构型设计模式
 * 它将一个对象的抽象部分与它的实现部分分离，使它们可以独立地变化。桥接模式的目的是将抽象与实现解耦，从而实现系统的灵活性和可扩展性。
 *
 * 将m*n个实现类转换为m+n个实现类
 * @author yuez
 * @since 2023/4/24
 */
public class 桥接模式 {
    /**
     * Shape 类充当桥接模式中的抽象部分，Color 接口充当实现部分。使用桥接模式，我们可以轻松地添加新的图形和颜色类，而不会影响现有的代码。
     * @param args
     */
    public static void main(String[] args) {
        Color red = new Red();
        Color blue = new Blue();

        Shape rectangle = new Rectangle(red);
        rectangle.draw();

        Shape circle = new Circle(blue);
        circle.draw();
    }
}
interface Color {
    void applyColor();
}

class Red implements Color{

    @Override
    public void applyColor() {
        System.out.println("red...");
    }
}
class Blue implements Color{

    @Override
    public void applyColor() {
        System.out.println("blue...");
    }
}
//接口嵌套（包含color 可以传递具体实现）
abstract class Shape{
    Color color;

    public Shape(Color color) {
        this.color = color;
    }

    abstract void draw();
}

class Rectangle extends Shape {
    public Rectangle(Color color) {
        super(color);
    }

    @Override
    void draw() {
        System.out.println("draw rectangle...");
        color.applyColor();
    }
}

class Circle extends Shape{

    public Circle(Color color) {
        super(color);
    }

    @Override
    void draw() {
        System.out.println("draw circle ....");
        color.applyColor();
    }
}