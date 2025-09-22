package com.example.text.java.stream;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * StreamCreateTest
 *
 * @author yuez
 * @since 2024/1/24
 */
public class StreamFuncTest {
    @Test
    public void function(){
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5,1);
        //filter
//        stream.filter(i -> i > 2).forEach(System.out::println);

        //map=>产生一个新的stream
//        stream.map(i -> i * 2).forEach(System.out::println);

        //FlatMap => flatMap() 方法类似于 map() 方法，不同之处在于它可以将每个元素映射为一个流，并将所有流连接成一个流。这主要用于解决嵌套集合的情况。
        //合并流
        List<List<Integer>> nestedList = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5, 6)
        );
//        nestedList.stream().flatMap(List::stream).forEach(System.out::println);

        //limit 截断
//        stream.limit(2).forEach(System.out::println);

        //skip 跳过
//        stream.skip(2).forEach(System.out::println);

        //sort 排序
//        stream.sorted().forEach(System.out::println);

        //distinct 去重
//        stream.distinct().forEach(System.out::println);

        //collect 汇总
        Stream<String> stream2 = Stream.of("apple", "banana", "cherry");
        List<String> collect = stream2.collect(Collectors.toList());

        //reduce 规约
        //reduce() 方法用于将 Stream 中的元素依次进行二元操作，得到一个最终的结果。它接受一个初始值和一个 BinaryOperator 函数作为参数。例如：
        stream.reduce(0, (a, b) -> a + b); //所有元素求和





    }

}
