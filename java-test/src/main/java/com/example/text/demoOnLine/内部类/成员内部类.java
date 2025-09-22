package com.example.text.demoOnLine.内部类;

/**
 * 成员内部类
 *
 * @author yuez
 * @since 2024/8/23
 */
public class 成员内部类 {
    public static void main(String[] args) {
        Outer2 outer2 = new Outer2();//不会调用内部类
        Outer2.Inner inner = outer2.new Inner();//加载{}
        Outer2.Inner inner2 = outer2.new Inner();//加载{}
    }
}
class Outer2{
    static{
        System.out.println("Outer class is loaded");
    }

    //11版本以后，不允许在成员内部类中定义静态成员
    public class Inner{
        {
            System.out.println("Inner class is loaded");
        }
    }
}
