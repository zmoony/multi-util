package com.example.text.java.stream;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * StreamCreateTest
 *
 * @author yuez
 * @since 2024/1/24
 */
public class StreamCreateTest {
    /**
     * 集合创建
     */
    @Test
    public void createFromCollection(){
        ArrayList<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        list.stream().forEach(System.out::println);
    }

    /**
     * 数组创建
     */
    @Test
    public void createFromArray(){
        String[] array = {"a", "b", "c"};
        Arrays.stream(array).forEach(System.out::println);
    }

    /**
     * stream.of()创建
     */
    @Test
    public void streamOf(){
        System.out.println(Stream.of("1", "2", "3"));
    }

    /**
     * builder创建
     */
    @Test
    public void streamBuilder(){
        Stream.Builder<String> builder = Stream.builder();
        builder.add("1");
        builder.add("2");
        builder.add("3");
        builder.build().forEach(System.out::println);
    }

    /**
     * 从 I/O 资源创建
     */
    @Test
    public void createFromIo() {
        Path path = Paths.get("C:\\Users\\yuez\\Desktop\\11.txt");
        try(Stream<String> lines = Files.lines(path) ){
            System.out.println(lines.collect(Collectors.joining(",")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
