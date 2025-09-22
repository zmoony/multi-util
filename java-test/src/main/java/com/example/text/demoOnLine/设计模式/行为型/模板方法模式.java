package com.example.text.demoOnLine.设计模式.行为型;

/**
 * 模板方法模式
 * 行为型设计模式
 * 它定义了一个算法的骨架，并将一些步骤延迟到子类中实现。模板方法模式使得子类可以在不改变算法结构的情况下重定义算法中的某些步骤。<br>
 * 场景：
 * <ul>
 * <li>算法骨架固定：如果一个算法的基本结构已经固定，但具体的实现步骤可能因为不同的场景而不同，这个时候可以使用模板方法模式。</li>
 * <li>实现代码复用：如果有多个类的某些方法结构相似，但是实现细节不同，这个时候可以将这些相同的结构抽象到父类中，由子类来实现不同的细节。</li>
 * <li>简化代码实现：模板方法模式可以将复杂的代码实现分离成几个简单的步骤，从而降低代码实现的难度和复杂度。</li>
 * <li>框架和库的设计：模板方法模式是设计框架和库的重要方式之一，它可以提供统一的接口和标准的实现流程，方便用户进行扩展和定制</li>
 * </ul>
 *结构：
 * <ul>
 *     <li>AbstractClass 是一个抽象类，它定义了算法的骨架，
 *     其中 templateMethod() 是模板方法，它定义了算法的流程，由一些抽象方法 primitiveOperation1() 和 primitiveOperation2() 组成。</li>
 *     <li>
 *         ConcreteClass 是 AbstractClass 的具体子类，它实现了抽象方法，定义了具体的算法细节。
 *         在客户端使用时，创建 ConcreteClass ，然后调用其 templateMethod() 方法，即可完成算法的执行。
 *     </li>
 * </ul>
 *
 * @author yuez
 * @since 2023/4/24
 */
public class 模板方法模式 {
    public static void main(String[] args) {
        AbstractClass abstractClass = new ConcreteClass();
        abstractClass.templateMethod();
    }
}
// 抽象类，定义算法骨架
abstract class AbstractClass{
    //模板方法，定义算法流程
    //需要注意的是，在模板方法模式中，模板方法通常被声明为 final，
    // 以防止子类对其进行重写。同时，由于模板方法是一个抽象方法，因此在实现时需要注意不同抽象方法的实现顺序，以确保算法的正确性。
    public final void templateMethod(){
        primitiveOperation1();
        primitiveOperation2();
    }

    //抽象方法1，子类实现
    public abstract void primitiveOperation1();
    //抽象方法2，子类实现
    public abstract void primitiveOperation2();
}
// 具体子类，实现具体的算法细节
class ConcreteClass extends AbstractClass{

    @Override
    public void primitiveOperation1() {
        System.out.println("子类方法1");
    }

    @Override
    public void primitiveOperation2() {
        System.out.println("子类方法2");
    }
}

