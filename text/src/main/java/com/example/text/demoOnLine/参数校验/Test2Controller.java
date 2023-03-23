package com.example.text.demoOnLine.参数校验;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 测试请求类
 *
 * @author yuez
 * @since 2023/2/21
 */
@RestController
public class Test2Controller {

    @GetMapping("/test2")
    public Student test(@RequestBody @Validated Student student){
        return student;
    }
    @PostMapping("/students")
    public Student create(@RequestBody @Validated(Student.Create.class) Student student){
        return student;
    }
    @PutMapping("/students")
    public Student update(@RequestBody @Validated(Student.Update.class) Student student){
        return student;
    }
}
