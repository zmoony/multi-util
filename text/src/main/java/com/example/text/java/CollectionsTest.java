package com.example.text.java;

import org.apache.commons.collections4.CollectionUtils;
import org.testng.annotations.Test;

import java.util.*;

/**
 * CollectionsTest
 *
 * @author yuez
 * @since 2022/8/18
 */
public class CollectionsTest {

    /**
     * Collections.singletonMap 不能更改
     */
    @Test
    public void SingleCollectionTest(){
        Map<Integer, Integer> map = Collections.singletonMap(11, 11);
        System.out.println(map.put(2,22));
    }

    @Test
    public void utils(){
        List<String> a= new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        a.add("d");
        a.add("e");
        System.out.println(a);
        List<String> b= new ArrayList<>();
        b.add("a");
        b.add("e");
        b.add("r");
        b.add("t");
        b.add("y");
        Collection<String> subtract = CollectionUtils.subtract(a, b);
        System.out.println(subtract);
        Collection<String> strings = CollectionUtils.retainAll(a, b);
        System.out.println(strings);

        List<String> passapplyBeans = new ArrayList<>();
        passapplyBeans.addAll(Collections.emptyList());
        System.out.println(passapplyBeans);

        System.out.println((System.currentTimeMillis()+"").length());
    }

}
