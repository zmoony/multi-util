package com.example.text.java.function.demo;

import com.example.text.java.stream.Runnable;

import java.util.function.Consumer;

/**
 * 函数式编程
 * <pre>
 * 使用注解@FunctionalInterface标识，并且只包含一个抽象方法的接口是函数式接口。函数式接口主要分为
        Supplier供给型函数(无参数有返回值)、
        Consumer消费型函数（有参数没有返回值）、
        Runnable无参无返回型函数和（无参无返回型）
        Function有参有返回型函数。（Function可以看作转换型函数）
 *</pre>
 * 不能与内部类混淆
 * 1. 内部类是有一个新的自己的作用域
 * 2. lambda他的作用域与外部作用域一致，所以lambda表达式可以访问外部作用域的变量，但是不能修改外部作用域的变量。
 *
 * lambda 应该就是简洁明了的一行展示。涉及到代码块的需要单拎出来。
 * <pre>
 *     Foo foo = param -> buildString(param) ;
 *     private String buildString(String param) {  String result = "Something " + param ;  // ...  return result ;}
 * </pre>
 *
 * 方法引用
 * 如果lambda调用的还是别的类的已经实现的方法，可以直接进行方法的引用
 * <pre>Function<String, String> func = String::toLowerCase;</pre>
 *
 * Effectively Final
 * 确保变量被赋值一次
 * lambda要求。内部访问 非final 变量会导致编译时错误。根据 "Effectively final" 的概念，只要变量仅被赋值一次，编译器就会将其视为 final。
 *
 * @author yuez
 * @since 2024/2/22
 */
public class 函数式编程 {
    public static void main(String[] args) {
        VUtils.isBlankOrNotBlank("123").presentOrElseHandle(System.out::println,()->{
            System.out.println("空");
        });
    }
}
@FunctionalInterface
interface PresentOrElseHandle<T extends Object>{
    /**
     * 不为空的时候进行消费操作
     * 为空的时候就行其他操作
     * @param action 不为空
     * @param emptyAction 为空
     */
    void presentOrElseHandle(Consumer<? super  T> action, Runnable emptyAction);
}

class VUtils{
    /**
     * 相当于写了function的实现类，并用来传递参数，将参数传递给具体的方法参数
     * @param str
     * @return
     */
    public static PresentOrElseHandle<?> isBlankOrNotBlank(String str){
        return ((consumer, runnable)->{
            if(str.isBlank()){
                runnable.run();
            }else{
                consumer.accept(str);
            }
        });
    }
}
