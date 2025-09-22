package com.example.text.java;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;

/**
 * MultiValueMap 一个键对应多个值
 *
 * @author yuez
 * @since 2022/7/22
 */
public class DataTypeTest {
    @Test
    public void MultiValueMapTest(){
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("1","1");
        map.add("1","2");
        System.out.println(map);
    }
}


