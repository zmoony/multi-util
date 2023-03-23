package com.example.text.demoOnLine.重复请求过滤;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试请求类
 *
 * @author yuez
 * @since 2023/2/21
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "ok";
    }
}
