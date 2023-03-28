package com.example.text.java;

import org.testng.annotations.Test;

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

}
