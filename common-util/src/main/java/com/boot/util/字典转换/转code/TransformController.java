package com.boot.util.字典转换.转code;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TransformController {

    private final DictMappingCache cache;

    @PostMapping("/convert")
    public Map<String,Object> convert(@RequestBody Map<String,Object> thirdData) {
        Map<String,Object> result = new HashMap<>();
        thirdData.forEach((k, v) -> {
            Object myValue = cache.toMyCode("sexMap", String.valueOf(v)); // 举例
            result.put(k, myValue != null ? myValue : v);
        });
        return result;
    }
}
