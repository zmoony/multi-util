package com.boot.util.mapUtil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ObjectMapper工具类
 *
 * @author yuez
 * @version 1.0.0
 * @className ObjectMapperUtil
 * @date 2021/3/13 16:41
 **/
public class ObjectMapperUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static XmlMapper xmlMapper = new XmlMapper();
    static {
        // 转换为格式化的json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
    }

    public static String obj2json(Object obj) throws Exception{
        return objectMapper.writeValueAsString(obj);
    }


    public static<T> T json2pojo(String jsonStr,Class<T> clazz) throws Exception{
        return objectMapper.readValue(jsonStr, clazz);
    }


    public static Map json2map(String jsonStr)throws Exception{
        return objectMapper.readValue(jsonStr, Map.class);
    }



    public static<T> List json2list(String jsonArrayStr, Class<T> clazz)throws Exception{
        List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr, new TypeReference<List<Map<String, Object>>>(){});
        List result = new ArrayList();
        for (Map map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }


    public static<T> T map2pojo(Map map,  Class<T> clazz){
        return objectMapper.convertValue(map, clazz);
    }


    public static String json2xml(String jsonStr)throws Exception{
        JsonNode root = objectMapper.readTree(jsonStr);
        String xml = xmlMapper.writeValueAsString(root);
        return xml;
    }

    public static<T> String object2xml(T t)throws Exception{
        return  xmlMapper.writeValueAsString(t);
    }

    //xml为xml里的内容---无法解析list
    public static String xml2json(String xml) throws JsonProcessingException {
        /*{"user":{"name":"张三","age":"18","date":"2020-03-20 12:00:00"},"user":{"name":"李四","age":"28","date":"2020-03-20 10:20:00"}}
        StringWriter w = new StringWriter();
        JsonParser jp;
        try {
            jp = xmlMapper.getFactory().createParser(xml);
            JsonGenerator jg = objectMapper.getFactory().createGenerator(w);
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
            jp.close();
            jg.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return w.toString();*/
//        JsonNode root = objectMapper.readTree(xml);
        JsonNode xml1 = xmlMapper.readTree(xml);
        return objectMapper.writeValueAsString(xml1);

    }

    public static<T> T xml2Object(Path path, Class<T> clazz) throws Exception {
        String xml=file2String(path);
        return xmlMapper.readValue(xml, clazz);
    }

    public static String xml2json(Path path) throws IOException {
        String xml=file2String(path);
        return xml2json(xml);
    }

    public static String file2String(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

}
