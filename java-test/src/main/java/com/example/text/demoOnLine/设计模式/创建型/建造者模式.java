package com.example.text.demoOnLine.设计模式.创建型;

import com.example.text.demoOnLine.设计模式.User;

/**
 * 建造者模式
 * 创建型设计模式
 * 它通过将一个复杂的对象的创建过程分解成多个简单的步骤，并将这些步骤封装到一个Builder对象中，从而可以灵活地创建不同的对象
 *
 * @author yuez
 * @since 2023/4/24
 */
public class 建造者模式 {
    public static void main(String[] args) {
        User user = new User.Builder()
                .setName("lili")
                .setEmail("ssss@sss")
                .setAge(10)
                .build();
    }
}




