package com.boot.websocket.controller;

import com.boot.websocket.vo.Params;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * MutilPartController
 *
 * @author yuez
 * @since 2024/11/4
 */
@RestController
@RequestMapping(value = "/mutil")
public class MutilPartController {
/*
    此方法无效，
    需要将file与参数分开。参数可以单独设置，也可以放在一个对象里，使用@ModelAttribute注解。

    @PostMapping(value = "/test")
    public String test(@RequestPart Params mutilPart) {
        System.out.println(mutilPart.getName());
        System.out.println(mutilPart.getFile().getSize());
        return "test";
    }
*/

    /**
     * 正常form传参，适用于复杂参数
     * @param file
     * @param mutilPart
     * @return
     */
    @PostMapping(value = "/test2")
    public String test2(@RequestPart MultipartFile file,  @ModelAttribute Params mutilPart) {
        System.out.println(mutilPart.getName());
        System.out.println(file.getSize());
        return "test";
    }

    /**
     * 适用于简单参数
     * @param file
     * @param name
     * @return
     */
    @PostMapping(value = "/test3")
    public String test3(@RequestPart MultipartFile file, @RequestPart String name) {
        System.out.println(name);
        System.out.println(file.getSize());
        return "test";
    }
}
