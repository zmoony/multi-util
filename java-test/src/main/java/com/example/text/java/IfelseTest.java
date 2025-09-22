package com.example.text.java;

import com.example.text.demoOnLine.设计模式.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 替换if-else
 *
 * @author yuez
 * @since 2023/3/17
 */
public class IfelseTest {

    @Test
    public void actionTest(){
        if(true){
            if(true){
                System.out.println("退出....");
                return;
            }
        }
        System.out.println("继续....");
    }


    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        String v = Optional.ofNullable(map.get("key")).map(String::toUpperCase).orElse("default");
        System.out.println(v);
    }
}



