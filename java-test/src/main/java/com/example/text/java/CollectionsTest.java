package com.example.text.java;

import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
//        Collection<String> subtract = CollectionUtils.subtract(a,b);
//        System.out.println(subtract);
//        Collection<String> strings = CollectionUtils.retainAll(a, b);
//        System.out.println(strings);

        List<String> passapplyBeans = new ArrayList<>();
        passapplyBeans.addAll(Collections.emptyList());
        System.out.println(passapplyBeans);

        System.out.println((System.currentTimeMillis()+"").length());
    }

    @Test
    public void SynchronizedListTest(){
        List<String> a= new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        a.add("d");
        a.add("e");
        System.out.println(a);
        List<String> b = Collections.synchronizedList(a);
        b.add("33");
        System.out.println(b);

        //add()等方法的时候是加了synchronized关键字的,但是listIterator(),iterator()却没有加.所以在使用的时候需要加上synchronized.
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
        list.add("1");
        list.add("2");
        list.add("3");

        synchronized (list) {
            Iterator i = list.iterator(); // Must be in synchronized block
            while (i.hasNext()) {
                //foo(i.next());
                System.out.println(i.next());
            }
        }

    }

    @Test
    public void copyOnWrite(){
        List<String> a= new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        a.add("d");
        a.add("e");
        System.out.println(a);

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(a);
        list.add("11");

        System.out.println(list);

        System.out.println(5/3);
         int[] table = new int[5];
    }

    @Test
    public void ListTest(){
        Deque<Integer> deque = new LinkedList<>();
        deque.add(1);
        deque.add(2);
        System.out.println(deque.removeFirst());
    }

    /**
     * 打乱顺序
     */
    @Test
    public void shuffle(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        Collections.shuffle(list);
        System.out.println(list.get(0));
    }

}
