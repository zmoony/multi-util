package com.example.boot3.jdk17;

import java.util.Scanner;

/**
 * yield是在JDK13之后正式加入到Java中的新的关键字，这个关键字的主要作用是进行内容的局部返回，现阶段其主要的设计是结合switch语句来使用的
 *
 * @author yuez
 * @since 2023/2/15
 */
public class YieldMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        int result = switch (next){
            case "1" -> 11;
            case "2" -> 22;
            case "3" ->{
                yield 33;
            }
            default -> 00;
        };
        System.out.println(result);
    }
}
