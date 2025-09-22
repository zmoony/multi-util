package com.example.text.demoOnLine.参数校验;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 测试请求类
 *
 * 区别	@Valid	@Validated
 * 来源	@Valid是Java标准注解	@Validated是Spring框架定义的注解。
 * 是否支持分组验证	不支持	支持
 * 使用位置	构造函数、方法、方法参数、成员属性	类、方法、方法参数，不能用于成员属性
 * 是否支持嵌套校验	支持	不支持
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
