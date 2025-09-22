package com.example.text.java.function;

import org.junit.Test;

/**
 * FunctionTest
 *
 * @author yuez
 * @since 2023/4/12
 */
public class FunctionTest {

    /**
     * if-else 抛出异常可这样处理
     */
    @Test
    public  void isTrue1(){
        VUtils.isTrue(true).throwMessage("异常抛出");
    }


    /**
     * if-else 分支处理
     */
    @Test
    public void isTrueOrFalse(){
        com.example.text.java.function.VUtils.isTrueOrFalse(true)
                .trueOrFalseHandle(()->{
                    System.out.println("true,开始运行");
                },()->{
                    System.out.println("false,开始运行");
                });
    }

    @Test
    public void isBlankOrNoBlank(){
        com.example.text.java.function.VUtils.isBlankOrNotBlank("kkk")
                .presentOrEleseHandle(System.out::println,()->{
                    System.out.println("空");
                });
    }



}
