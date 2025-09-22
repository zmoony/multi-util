package com.example.text.java;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * SetTest
 *
 * @author yuez
 * @since 2024/11/6
 */
public class SetTest {

    /**
     * 基于哈希表实现
     * 不保证顺序
     * 允许一个null
     * [场景]
     * 去重 快速插入，删除，查找。不关系顺序
     */
    @Test
    public void HashSetTest(){
        Set<String> hashSet = new HashSet<>();
        hashSet.add("4");
        hashSet.add("1");
        hashSet.add("2");
        hashSet.add("2");
        hashSet.add("3");
        System.out.println("HashSetTest" + hashSet);
    }

    /**
     * 基于哈希表+链表实现
     * 保证顺序
     * 允许一个null
     * [场景]
     * 去重 快速插入，删除，查找。需要保持插入的顺序
     */
    @Test
    public void LinkedHashSetTest(){
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("4");
        linkedHashSet.add("1");
        linkedHashSet.add("2");
        linkedHashSet.add("2");
        linkedHashSet.add("3");
        System.out.println("LinkedHashSetTest" + linkedHashSet);
    }

    /**
     * 基于红黑树实现
     * 自然排序/自定义排序
     * 不允许null
     * [场景]
     * 去重，排序，支持范围查询
     */
    @Test
    public void TreeSetTest(){
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("4");
        treeSet.add("1");
        treeSet.add("2");
        treeSet.add("2");
        treeSet.add("3");
        System.out.println("TreeSetTest" + treeSet);
    }

    /**
     * 基于枚举实现
     * 不允许出现null
     * [场景]
     * 更高效的操作
     */
    @Test
    public void EnumSetTest(){
        Set<String> enumSet = new TreeSet<>();
        enumSet.add("4");
        enumSet.add("1");
        enumSet.add("2");
        enumSet.add("2");
        enumSet.add("3");
        System.out.println("TreeSetTest" + enumSet);
    }

    /**
     * 适用于并发的场景
     */
    @Test
    public void ConcurrentSkipListSetTest(){
        Set<String> concurrentSkipListSet = new ConcurrentSkipListSet<String>();
        concurrentSkipListSet.add("4");
        concurrentSkipListSet.add("1");
        concurrentSkipListSet.add("2");
        concurrentSkipListSet.add("2");
        concurrentSkipListSet.add("3");
        System.out.println("ConcurrentSkipListSetTest" + concurrentSkipListSet);
    }

    /**
     * 线程安全
     * 每次修改都会产生一个新的数组（CopyOnWriteArraySet：修改时会创建新的内部数组，但外部引用仍然指向同一个 CopyOnWriteArraySet 对象，因此引用不会改变。）
     * 适用于读多写少
     */
    @Test
    public void CopyOnWriteArraySetTest(){
        Set<String> copyOnWriteArraySet = new CopyOnWriteArraySet<String>();
        copyOnWriteArraySet.add("4");
       //查看对象地址
        System.out.println(copyOnWriteArraySet==copyOnWriteArraySet);//true
        copyOnWriteArraySet.add("1");
        System.out.println(copyOnWriteArraySet==copyOnWriteArraySet);//true
        copyOnWriteArraySet.add("2");
        copyOnWriteArraySet.add("2");
        copyOnWriteArraySet.add("3");
        System.out.println("CopyOnWriteArraySetTest" + copyOnWriteArraySet);
    }


}
