package com.example.text.java.stream;

import java.util.*;

/**
 * WiscomStream
 * null为通用类型，强转任何类型都不会报错
 * @author yuez
 * @since 2024/2/1
 */
public class WiscomStream
{
    public static Map<String, Object> jsonTest(Map<String, Object> toMap){
//        Map<String, Object> toMap = JsonToObjectUtil.jsonToMap(s);
//        Map<String, Object> toMap = new HashMap<>();
       return Optional.ofNullable((List<Map<String, Object>>) toMap.get("arguments"))
                .map(list2 -> list2.get(0))
                .map(map1 -> (Map<String, Object>) map1.get("ret"))
                .map(map2 -> (List<Map<String, Object>>) map2.get("subjects"))
                .map(list1 -> list1.get(0))
                .map(map3 -> (Map<String, Object>) map3.get("parts"))
                .map(map3 -> (Map<String, Object>) map3.get("MOTOR_VEHICLE"))
                .map(map4 -> (Map<String, Object>) map4.get("attrs"))
                .orElse(Collections.emptyMap());
    }


    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("arguments",Collections.emptyMap());
        System.out.println(jsonTest(map));

//        System.out.println((List<Map<String, Object>>)null);
    }
}
