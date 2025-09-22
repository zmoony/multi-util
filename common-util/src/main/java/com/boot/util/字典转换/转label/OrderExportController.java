package com.boot.util.字典转换.转label;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderExportController {

    private final DictConvertor dictConvertor;
    private final RestTemplate restTemplate;

    /** 假设订单原始数据 Map 字段有 status、payType */
    @PostMapping("/order/export")
    public void export(@RequestBody List<Map<String,Object>> orders) {
        // 1. 定义需要转换的字段与字典类型
//        Map<String,String> schema = Map.of(
//                "status", "ORDER_STATUS",
//                "payType", "PAY_TYPE"

        Map<String,String> schema = new HashMap<String, String>();
        schema.put("status", "ORDER_STATUS");
        schema.put("payType", "PAY_TYPE");


        // 2. 并行转换（量大就开并行）
        List<Map<String,Object>> converted = orders.parallelStream()
                .map(o -> dictConvertor.convert(o, schema))
                .collect(Collectors.toList());

        // 3. 发送给第三方
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(converted, headers);
        restTemplate.postForLocation("https://third.com/api/receive", entity);
    }
}
