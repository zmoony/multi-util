package com.example.text.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
        String ids = "12341,2342343";
        ids=ids.replaceAll("[^,]+","'$0'");
        System.out.println(ids);
    }

    /**
     * 25 男士
     * 45 女士
     */
    @Test
    public void numAdept(){
        int total = 231;
        int min = 131;
        int [] nums = new int[2];
        int sum=0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                 sum = 25*i + 45*j;
                if (sum == total){
                    System.out.println("【完美】男士25，次数："+i+";女士45，次数："+j);
                }
                if(sum > total){
                    break;
                }
                if(total - sum < min){
                    min = total - sum;
                    nums[0] = i;
                    nums[1] = j;
                }

            }
        }
        System.out.println("【最接近】差额："+min+";男士25，次数："+nums[0]+";女士45，次数："+nums[1]+";");
        System.out.println("计算结束");
    }

    @Test
    public void mdoTest () {
        System.out.println("4%4="+4%4);
        System.out.println("4/4="+4/4);
        System.out.println("4%5="+4%5);
        System.out.println("4/5="+4/5);
        System.out.println("4%8="+4%8);
        System.out.println("4/8="+4/8);
    }


    @Test
    public void templateMessage () {
        String content = "我是{name},我今年{age}了";
        String name = "小明";
        String age = "18";
        content = content.replace("{name}",name).replace("{age}",age);
        System.out.println(content);

    }

    @Test
    public void templateMessage2 () {
        String content = "我是{name},我今年{age}了";
        String name = "小明";
        String age = "18";


    }





}
