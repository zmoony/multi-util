package com.example.boot3.jdk17;

/**
 * 8版本的空指针---直接抛空指针异常，不具体到变量
 * 17版本的空指针--会具体到变量
 *
 * @author yuez
 * @since 2023/2/15
 */
public class NullPointMain {
    public static void main(String[] args) {
        String s = null;
        s.toLowerCase();
    }
}
