package com.example.boot3.jdk17;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合的操作
 *
 * @author yuez
 * @since 2023/2/15
 */
public class CollectionAndMapMain {
    @Test
    public void listTests(){
        List<Integer> list = List.of(1, 2, 3);
        System.out.println(list);
//        Set<Integer> set = Set.of(1, 1, 2, 3, 4, 5);//如果值重复--java.lang.IllegalArgumentException: duplicate element: 1
        Set<Integer> set = Set.of(1, 2, 3, 4, 5);
        System.out.println(set);
        Map<Integer, Integer> integerIntegerMap = Map.of(1, 1, 2, 3);
        System.out.println(integerIntegerMap);
        Map<Integer, Integer> integerIntegerMap1 = Map.ofEntries(Map.entry(1, 2), Map.entry(3, 4));
        System.out.println(integerIntegerMap1);
        //最多添加参数的个数
        for (int i = 0; i < 100; i++) {

        }
    }
}
