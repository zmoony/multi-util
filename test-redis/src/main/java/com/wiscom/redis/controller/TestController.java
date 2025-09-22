package com.wiscom.redis.controller;

import com.sun.javafx.collections.MappingChange;
import com.wiscom.redis.log.LogRecord;
import com.wiscom.redis.log.Loggable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TestController
 *
 * @author yuez
 * @since 2023/12/26
 */
@RestController
//@RequestMapping("/test")
public class TestController {

    @Loggable("测试调用数组传参")
    @LogRecord(subType = "#param[0]",type = "数组",success = "'success:'.concat(#param)",bizNo = "1",actionType = "get")
    @PostMapping("/paramArray")
    public String testParamArray(@RequestBody String[] param) {
        return "testParamArray";
    }

    @RequestMapping("/sayHello")
    public String sayHello( String name) {
        return "Hello " + name;
    }

    @RequestMapping("/{name}")
    public String sayHello2( @PathVariable("name") String name) {
        return "Hello " + name;
    }

    @PostMapping("/send/scenic/flow")
    public String sayHello2( @RequestBody Map<String,Object> param) {
        System.out.println(param);
        return "ok";
    }

}
