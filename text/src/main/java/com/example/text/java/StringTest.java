package com.example.text.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.objects.NativeString;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * string
 *
 * @author yuez
 * @since 2022/8/1
 */
public class StringTest {

    /**
     * 不存在会报  数组边界溢出的异常
     */
    @Test
    public void substringTest(){
        /*String sur_level="ddddd";
        String substring = sur_level.substring(sur_level.lastIndexOf("."));
        System.out.println(substring);*/
        /*String str= "jdbc:clickhouse://ip:port/wiscom";
        String database = str.substring(str.lastIndexOf("/")+1);
        System.out.println(database);*/
        BigDecimal id=new BigDecimal("32072400001320194049");
        List<BigDecimal> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            id = id.add(new BigDecimal(1));
            list.add(id);
        }
        String collect = list.stream().map(ids -> ids.toString()).collect(Collectors.joining(","));
        System.out.println(collect);
    }
    @Test
    public void splitTest(){
        String sur_level="ddddd";
        String[] substring = sur_level.split("\\.");
        System.out.println(Arrays.toString(substring));
        System.out.println(substring[substring.length-1]);
    }

    @Test
    public void splitTest2(){
        String sur_level="23435465564@wiscom@87857987";
        String[] split = sur_level.split("@wiscom@");
        for (String s : split) {
            System.out.println(s);
        }
    }

    @Test
    public void regexTest(){
        String plate_no="苏A 苏bbb     苏33";
        String plateNos = plate_no.replaceAll("\\s+", ",");
        System.out.println(plateNos);
    }

    @Test
    public void Indextest(){
        String sur_level="苏AD7800";
        System.out.println(sur_level.charAt(3-1));
        System.out.println(Arrays.asList(null,"-1",3,"4","sdsfds").contains(-1));
    }

    @Test
    public void test1() throws JsonProcessingException {
        String name="\u8fd9\u662f\u539f\u59cb\u7684\u6570\u636e\uff01\uff01\uff01";
        System.out.println(new String(name));
        System.out.println(name);
        String s = "\\u67e5\\u8be2\\u6210\\u529f";
        System.out.println(s); // \u67e5\u8be2\u6210\u529f


        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".jpg");

        String idcard = "321202198903193034";
        System.out.println(idcard.substring(6,10)+"-"+idcard.substring(10,12)+"-"+idcard.substring(12,14));

        Map<String, Object> image = new HashMap<>();
        image.put("ids", Collections.singletonList("11111")); //图片id
        ObjectMapper objectMapper = new ObjectMapper();
        String s1 = objectMapper.writeValueAsString(image);
        System.out.println(s1);
    }




}
