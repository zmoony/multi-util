package com.example.text.java;

import com.example.text.restdoc.Result2;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;

import java.util.*;

/**
 * <li>map的对象引用</li>
 * <li>线程安全-方法变量会不会串用</li>
 * @author yuez
 * @since 2022/6/17
 */
public class HeadMapTest {
    @Test
    public void mapTest(){
        Map<String, Result2> map = new HashMap<>();
        map.put("1",new Result2(200,"1"));
        map.put("2",new Result2(500,"2"));
        System.out.println(map.get("1"));
        Result2 result2 = map.get("1");
        result2.setCode(201);//*******************map里的对象也会改变，改变的是同一个对象
        System.out.println(map.get("1"));
    }

    /**
     * computeIfAbsent(key,function)
     * key 存在，返回value
     * 不存在 运行function，返回V，可后续添加值
     */
    @Test
    public void TestComputeIfAbsent (){
        HashMap<String, Set<String>> hashMap = new HashMap<>();
        hashMap.computeIfAbsent("china", key -> {return new HashSet<>();}).add("liSi");
        hashMap.computeIfAbsent("china", key -> {return new HashSet<>();}).add("zhangsna");
        hashMap.computeIfAbsent("tianjin", key -> {return new HashSet<>();}).add("shabi");
        System.out.println(hashMap.toString());
    }

    /**
     * omputeIfAbsent(key,function) key 存在，运行function，返回V，;不存在不操作
     */
    @Test
    public void TestComputeIfPresent (){
        HashMap<String, Set<String>> hashMap = new HashMap<>();
        hashMap.computeIfAbsent("china", key -> {return new HashSet<>();}).add("liSi");
        hashMap.computeIfPresent("china", (k,v)->{v.add("22");return v ;});
        hashMap.computeIfPresent("china2", (k,v)->{v.add("33");return v ;});

        System.out.println(hashMap.toString());
    }

    /**
     * compute完成以后的常规操作
     */
    @Test
    public void TestCompute (){
        HashMap<String, Set<String>> hashMap = new HashMap<>();
        hashMap.computeIfAbsent("china", key -> {return new HashSet<>();}).add("liSi");
        hashMap.compute("china",(k,v)->{
            v.add("33");
            return v;
        });
        hashMap.compute("china2",(k,v)->{
           return new HashSet<>();
        });

        System.out.println(hashMap.toString());
    }

    /**
     * putIfAbsent 不存在就把v给k,但需要注意的是：k不存在返回的是null,不能直接add,会空指针
     */
    @Test
    public void TestPutIfAbsent (){
        HashMap<String, Set<String>> hashMap = new HashMap<>();
        hashMap.putIfAbsent("china", new HashSet<>());
        hashMap.putIfAbsent("china", new HashSet<>()).add("sasa");

        System.out.println(hashMap.toString());
    }


    /**
     * 自动的一个key对应多个value
     */
    @Test
    public void TestMultiValueTest(){
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("1","1");
        map.add("1","2");
        map.add("1","3");
        map.add("1","4");
        map.add("2","22");
        System.out.println(map);
        System.out.println("获取第一个：get first:"+map.getFirst("1"));
        map.set("3","3");
        map.set("3","31");
        System.out.println("只会保存最后一个：set:"+map);

    }

    @Test
    public void orderMap(){
        Map<String,String> map = new LinkedHashMap<>();
        map.put("lili","ooo");
        map.put("rr","yyy");
        map.put("gg","nnn");
        System.out.println(map);

        map.put("gg","0");
        map.put("lili","0");
        map.put("rr","-1");
        System.out.println(map);

    }

    @Test
    private void test12(){
        System.out.println( getCacheKey("32012320391","20221226095315"));
        System.out.println( getCacheKey("32012320391","2022-12-26 09:53:15"));
    }

    public static String getCacheKey(String apeId,String passTime){
        if(passTime != null && passTime.contains("-")){
            passTime = passTime.replaceAll("-","").replaceAll("\\s*","").replaceAll(":","");
        }
        return apeId+":"+passTime.substring(0,Math.min(passTime.length(),10));
    }


}
