package com.example.boot3.jdk17;

/**
 * record给类自动加上get,set方法
 *
 * @author yuez
 * @since 2023/2/15
 */
public class RecordMain {
    public static void main(String[] args) {
        A a = new A("wiscom",1,1);
        System.out.println(a.age());
    }
}
record A(String name,int age,int classes) {

}