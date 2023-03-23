package com.example.text.restdoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@Tag(name = "HelloController",description = "测试接收接口")
@RestController
@RequestMapping("doc")
public class Hello2Controller {

    @Operation(summary = "hello",description = "无需权限")
    @RequestMapping(value = "hello",method = RequestMethod.GET)
    @Parameter(name = "name",description = "姓名")
    public Result2 hello(@RequestParam("name") String name) {
        return new Result2(200, String.format("Hello %s!", name));
    }

    @RequestMapping(value = "hello/test",method = RequestMethod.GET)
    public String hello2(@RequestParam("name") String name) {
        return "ok"+name;
    }
}
