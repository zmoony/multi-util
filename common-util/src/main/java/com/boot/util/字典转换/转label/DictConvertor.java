package com.boot.util.字典转换.转label;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class DictConvertor {

    private final DictCache dictCache;

    /**
     * 将原始 Map 转换成带描述的 Map
     * @param source 原始业务 Map
     * @param schema 字段->字典类型的映射
     * @return 转换后的 Map（深拷贝，线程安全）
     */
    public Map<String, Object> convert(Map<String, Object> source,
                                       Map<String, String> schema) {
        Map<String, Object> target = new HashMap<>(source);
        schema.forEach((field, dictType) -> {
            Object value = target.get(field);
            if (value == null) return;
            if (value instanceof Collection) {
                List<String> labels = ((Collection<?>) value).stream()
                        .map(v -> dictCache.label(dictType, String.valueOf(v))
                                           .orElse(String.valueOf(v)))
                        .collect(Collectors.toList());
                target.put(field, labels);
            } else {
                dictCache.label(dictType, String.valueOf(value))
                        .ifPresent(label -> target.put(field, label));
            }
        });
        // 处理嵌套 Map / List<Map> 一般用不到可以去除
        target.replaceAll((k, v) -> deepConvert(v, schema));
        return target;
    }

    private Object deepConvert(Object v, Map<String, String> schema) {
        if (v instanceof Map) {
            return convert((Map<String, Object>) v, schema);
        }
        if (v instanceof Collection) {
            return ((Collection<?>) v).stream()
                    .map(e -> deepConvert(e, schema))
                    .collect(Collectors.toList());
        }
        return v;
    }

    /**
     * 提取字段->字典类型的映射 使用注解提取schema
     * @param clazz
     * @return
     */
    public static Map<String,String> extractSchema(Class<?> clazz) {
        Map<String,String> map = new HashMap<>();
        ReflectionUtils.doWithFields(clazz, f -> {
            Dict d = f.getAnnotation(Dict.class);
            if (d != null) {
                String target = StringUtils.hasText(d.target()) ? d.target() : f.getName();
                map.put(target, d.type());
            }
        });
        return map;
    }
}
