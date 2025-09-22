package com.example.text.java.function.demo;

import java.util.function.Function;

/**
 * FunctionTest
 *
 * @author yuez
 * @since 2024/12/2
 */
public class FunctionTest {

    public static void main(String[] args) {
        String result = new FunctionTest().pack("hello", (String str)->{
            return "pack:" + str;
        });
        System.out.println(result);
    }

    public String pack (String param, Function<String,String> foo) {
        return foo.apply(param);
    }
}

/**
 * 项目管理需要
 * 创建一个接口，该接口只包含一个抽象方法。
 * 使用该接口，能防止你再定义其它方法。
 * 不建议有过多的default方法
 */
@FunctionalInterface
interface Foo {
    String apply(String str);
}




















