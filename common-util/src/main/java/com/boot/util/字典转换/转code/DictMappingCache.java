package com.boot.util.字典转换.转code;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DictMappingCache {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ResourceLoader resourceLoader;

    /** 大类 -> thirdCode -> myCode */
    private volatile Map<String, Map<String, Object>> index = new HashMap<>();

    @Value("${dict.json.path:classpath:dict.json}")
    private String path;

    public DictMappingCache(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void load() {
        reload();
    }

    public void reload() {
        try {
            Resource res = resourceLoader.getResource(path);
            Map<String, List<DictItem>> raw = objectMapper.readValue(
                    res.getInputStream(),
                    new TypeReference<Map<String, List<DictItem>>>() {});

            Map<String, Map<String, Object>> idx = new ConcurrentHashMap<>();
            raw.forEach((type, list) -> {
                Map<String, Object> m = new ConcurrentHashMap<>();
                list.forEach(item -> m.put(item.getThirdCode(), item.getMyCode()));
                idx.put(type, m);
            });
            index = idx;
        } catch (IOException e) {
            throw new IllegalStateException("加载 dict.json 失败", e);
        }
    }

    /**
     * 根据大类 + 第三方值 获取我方值
     */
    public Object toMyCode(String type, String thirdCode) {
        return index.getOrDefault(type, Collections.emptyMap()).get(thirdCode);
    }
}
