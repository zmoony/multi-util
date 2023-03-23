package com.boot.util.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className ColleactionUtils
 * @date 2021/4/2 10:59
 **/
public class CollectionUtils {
    public static boolean isEmpty(Collection collection){
        if(collection == null || collection.isEmpty()){
            return true;
        }
        return false;
    }
    public static boolean isEmpty(Map map){
        if(map == null || map.isEmpty()){
            return true;
        }
        return false;
    }
    public static boolean isNotEmpty(Collection collection){
        if(collection == null || collection.isEmpty()){
            return false;
        }
        return true;
    }
    public static boolean isNotEmpty(Map map){
        if(map == null || map.isEmpty()){
            return false;
        }
        return true;
    }
}
