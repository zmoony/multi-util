package com.example.text.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mapTest
 *
 * @author yuez
 * @since 2022/8/23
 */
@Log4j2
public class MapTest {

    @org.testng.annotations.Test
    public void mapToObjectTest(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","lili");
        map.put("age",18);
        Test test = mapToObject(map, Test.class);
        System.out.println(test.toString());
    }

    @org.testng.annotations.Test
    public void nullTest(){
        Map<String, List<String>> map = new HashMap<>();
        List<String> strings = map.get("1");
        for (String string : strings) {
            System.out.println(string);
        }
    }


    @org.testng.annotations.Test
    public void getOrDefaultTest(){
        Map<String,Object> map_kafka = new HashMap<>();
//        map_kafka.put("bk_card_id","");
        String bk_card_id = map_kafka.getOrDefault("bk_card_id", "321000")+"";
        map_kafka.put("place_code", bk_card_id.substring(0,Math.min(bk_card_id.length(),6)));
        System.out.println(map_kafka.get("place_code"));
    }

    public static <T> T mapToObject(Map<String,Object> json, Class<T> clazz){

        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.convertValue(json,clazz);
        }catch (Exception ex) {
            log.error("json解析成"+clazz+"失败："+ex.getMessage());
            return null;
        }
    }
}

@Data
class Test{
    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}