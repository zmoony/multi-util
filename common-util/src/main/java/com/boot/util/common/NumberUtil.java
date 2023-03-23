package com.boot.util.common;

/**
 * 数值
 *
 * @author yuez
 * @version 1.0.0
 * @className NumberUtil
 * @date 2021/3/18 18:55
 **/
public class NumberUtil {

    //一个数能被哪些数整除
    public static void division(int t){
        int j = 0;
        for(int i=1; i<=t; i++) {
            if(t%i==0) {
                System.out.println(t + " 可以被 " + i + " 整除，商为 " + t/i + "。");
                j++;
            }else if(j==0) {
                System.out.println(t + "没有可以被整除的数。");
            }
        }
    }

    public static void main(String[] args) {
        division(10000000-9922230);
    }
}
