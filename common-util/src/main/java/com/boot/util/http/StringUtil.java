package com.boot.util.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lrJiang
 * @Description:
 * @date 2022/5/19
 */
@Log4j2
public class StringUtil {
    private static String URL_AND_1 = "&";
    private static String URL_AND_2 = "?";

    public static boolean isEmpty(String testString) {
        return null == testString || testString.trim().isEmpty();
    }

    public static boolean isNotEmpty(String testString) {
        return !isEmpty(testString);
    }

    public static String urlGetParamGenerator(String url, Map<String, Object> param) {
        url = Objects.isNull(url) ? "" : url;

        //按规则先拼接好参数,得到参数字符串，例如：paramA=123&paramB=345
        String paramInUrlString = param.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));

        //给url加上?
        url = url.contains("?") ? url : (url + "?");

        if (url.endsWith(URL_AND_1) || url.endsWith(URL_AND_2)) {
            url += paramInUrlString;
        } else {
            url += "&" + paramInUrlString;
        }

        return url;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String objectToJson(Object param) {
        try {
            return OBJECT_MAPPER.writeValueAsString(param);
        } catch (JsonProcessingException ex) {
            log.error("Object解析成json失败：" + ex.getMessage());
            return "";
        }
    }

    public static List<Object> jsonToList(String jsonString) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, Object.class);
            return OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (JsonProcessingException ex) {
            log.error("json解析成list失败：" + ex.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    public static Map<String, Object> jsonToMap(String jsonString) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class);
            return OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (JsonProcessingException ex) {
            log.error("json解析成map失败：" + ex.getMessage());
            return Collections.EMPTY_MAP;
        }
    }

    public static String md5Encode(String content) {
        return isEmpty(content) ? null : DigestUtils.md5Hex(content);
    }
}
