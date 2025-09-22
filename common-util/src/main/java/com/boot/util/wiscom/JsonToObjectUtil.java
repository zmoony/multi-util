/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
@Log4j2
public class JsonToObjectUtil {
    /**
     * 转换为Map<String, Object>
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json){
        Map<String, Object> map = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (JsonGenerationException | JsonMappingException ex) {
            log.error("json解析成map失败："+ex.getMessage());
        } catch (IOException ex) {
            log.error("json解析成map失败："+ex.getMessage());
        }
        return map;
    }

    public static <T> T jsonToObject(String json,Class<T> clazz){

        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.readValue(json, clazz);
        } catch (JsonGenerationException | JsonMappingException ex) {
            log.error("json解析成"+clazz+"失败："+ex.getMessage());
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public static <T> T mapToObject(Map<String,Object> json,Class<T> clazz){

        try {
            ObjectMapper mapper = new ObjectMapper();
            return  mapper.convertValue(json,clazz);
        }catch (Exception ex) {
            log.error("json解析成"+clazz+"失败："+ex.getMessage());
            return null;
        }
    }





    /**
     * 耗时小于 jsonListMotorvehicleBean
     * @param json
     * @return
     */
    public static List<Object> jsonListObject(String json){
        List<Object> list=null;
        try {

            ObjectMapper mapper = new ObjectMapper();
           // byte[] bytes = json.getBytes("UTF-8");
          //  list = mapper.readValue(bytes, new TypeReference<List<String>>(){});
            list = mapper.readValue(json, new TypeReference<List<Object>>(){});
        } catch (JsonGenerationException | JsonMappingException ex) {
            log.error("json解析成List失败："+ex.getMessage());
        } catch (IOException ex) {
            log.error("json解析成List失败："+ex.getMessage());
        }
        return list;
    }

    /*
    ** 使用   list = mapper.readValue(json, new TypeReference<List<T>>(){}); 返回的永远都是LinkedHashMap
    *  TypeReference是编译的，T是运行的，所以无效
    *  需要是应用JavaType指定。
     */
    public static <T> List<T> jsonList(String json,Class<T> clazz){
        List<T> list;
        try {
            ObjectMapper mapper = new ObjectMapper();
            list = mapper.readValue(json, new TypeReference<List<T>>(){});
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            list = mapper.readValue(json, type);
        } catch (JsonGenerationException | JsonMappingException ex) {
            log.error("json解析成List失败："+ex.getMessage());
            list=null;
        } catch (IOException ex) {
            log.error("json解析成List失败："+ex.getMessage());
            list=null;
        }
        return list;
    }
}
