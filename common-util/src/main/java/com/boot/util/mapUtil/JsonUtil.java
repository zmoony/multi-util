package com.boot.util.mapUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zy on 2019/1/15.
 */
@Log4j2
public class JsonUtil {
    private static String path = "wiscom-yisa-map.json";
    private static String defaultCode = "999";
    private static Map<String,List<FieldCodeBean>> map = new HashMap<>();
    @Data
    static class FieldCodeBean {
        private String wiscom;
        private String yisa;
        private String discribtion;
    }
    private static Map<String,Map<String,FieldCodeBean>> constmap=new HashMap<>();

    static{
        String reader = null;
        try {
            reader = FileUtils.readFileToString(new File(path), Charset.defaultCharset());
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(reader, new TypeReference<Map<String,List<FieldCodeBean>>>() {});
            if(map==null){
                log.error("读取字典映射配置文件出错。");
                System.exit(-1);
            }
            map.forEach((s, propBeans) -> {
                constmap.put(s,convertListToMap(propBeans));
            });
            log.debug(constmap);
        } catch (IOException e) {
            log.error("json解析错误,强制退出",e);
            System.exit(0);
        }
    }

    private static Map<String, FieldCodeBean> convertListToMap(List<FieldCodeBean> propBeans) {
        return propBeans.stream().collect(Collectors.toMap(FieldCodeBean::getYisa, propBean->propBean));
    }

    public static String covertHuaWeiToWiscom(String type, String value) {
        Map<String, FieldCodeBean> data  = constmap.get(type);
        return Optional.ofNullable(data)
                .map(m->m.get(value))
                .map(FieldCodeBean::getWiscom)
                .filter(s-> !StringUtils.isEmpty(s))
                .orElse(null);
    }





}
