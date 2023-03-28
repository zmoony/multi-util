package com.example.text.java;

import java.util.regex.*;
/**
 * 正则表达式
 * Pattern：正则表达式的编译表示，提供了对正则表达式的解析和匹配操作。
 * Matcher：匹配器，用于对字符串进行匹配操作。
 *
 * 定义正则表达式
 * 将正则表达式编译成一个Pattern对象
 * 使用Pattern对象匹配指定的字符串，生成一个Matcher对象
 * 通过Matcher对象进行匹配操作，得到匹配结果
 *
 *
 *
 * @author yuez
 * @since 2023/2/24
 */
public class RegularTest {
    public static void main(String[] args) {
        String pattern = "^[0-9]{12}11[0-9]{19}$";
        String str = "321000000000112023022320073810000";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        System.out.println(m.matches());
    }
}
