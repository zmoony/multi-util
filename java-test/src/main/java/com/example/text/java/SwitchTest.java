package com.example.text.java;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * switch
 *
 * @author yuez
 * @since 2022/10/9
 */
public class SwitchTest {

    /**
     * {@link java.lang.NullPointerException}  when the variable is null
     */
    @Test
    public void nullSwitch(){
        String str = null;
        switch (str){
            default:
                System.out.println("1111");
        }
        System.out.println(2222);
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        list.forEach(e->{
//            if (e == 7) {
//                System.out.println("准备推出...");
//                return;
//            }
//        });
        for (Integer e : list) {
            if (e == 7) {
                System.out.println("准备推出...");
                return;
            }
        }
        System.out.println("退出后...");//forEach会执行,for in 不会
    }

}
