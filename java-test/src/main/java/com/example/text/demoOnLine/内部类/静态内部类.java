package com.example.text.demoOnLine.内部类;

/**
 * 静态内部类
 *
 * @author yuez
 * @since 2024/8/23
 */
public class 静态内部类 {
    public static void main(String[] args) {
        //Inner class is loaded 不依赖于外部类
        Outer.Inner inner = new Outer.Inner();
    }
}

class Outer{
    static{
        System.out.println("Outer class is loaded");
    }

    static class Inner{
        static{
            System.out.println("Inner class is loaded");
        }
    }
}
