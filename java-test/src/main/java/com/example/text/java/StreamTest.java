package com.example.text.java;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * StreamTest
 *
 * @author yuez
 * @since 2022/8/18
 */
public class StreamTest {
    @Test
    public void mapTest() {
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(new HashMap<String, Object>() {{
            put("11", 22);
        }});
        rows.add(new HashMap<String, Object>() {{
            put("22", 22);
        }});
        List<Object> sss = rows.stream().map(m -> m.put("11", 1)).collect(Collectors.toList());
        System.out.println(sss);
    }

    @Test
    public void nullStreamTest() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> ape_id = result.stream().map(map -> map.get("ape_id").toString()).collect(Collectors.toList());
        System.out.println(ape_id);
    }

    @Test
    public void pageLimitTest() {
        int page = 4;
        int limit = 2;
        int from = (page - 1) * limit;
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> collect = list.stream().skip(from).limit(limit).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void nullTest() {
        List<Integer> list = Arrays.asList(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> collect = list.stream().collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void groupByTest() {
        List<A> list = Arrays.asList(
                new A("lili", "13", "aa"),
                new A("lili", "13", "cc"),
                new A("lili", "13", "bb"),
                new A("lili", "14", "bb"),
                new A("fafa", "13", "aa"),
                new A("wuwu", "14", "bb")
        );

        Map<String, List<A>> map_singleFiled = list.stream().collect(Collectors.groupingBy(e -> {
            return e.name;
        }));
        Map<String, List<A>> map_mergeFiled = list.stream().collect(Collectors.groupingBy(e -> {
            return e.name+"_"+e.age;
        }));
        Map<String, Map<String, List<A>>> map_mergeFiled2 = list.stream()
                .collect(Collectors.groupingBy(A::getName, Collectors.groupingBy(A::getAge)));
        Map<String, Set<String>> collect = list.stream()
                .collect(Collectors.groupingBy(A::getName, Collectors.mapping(A::getAge, Collectors.toSet())));


        System.out.println(map_singleFiled);
        System.out.println(map_mergeFiled);
        System.out.println(map_mergeFiled2);
        System.out.println(collect);
    }

}

@NoArgsConstructor
@AllArgsConstructor
@Getter
class A {
    String name;
    String age;
    String address;

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}