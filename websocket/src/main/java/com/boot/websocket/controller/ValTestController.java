package com.boot.websocket.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ValTestController
 *
 * @author yuez
 * @since 2024/11/14
 */
@RestController
@Log4j2
//@Scope("prototype")
//@Scope("request")
//@Scope("session")
@Scope("globalSession")
public class ValTestController {

    public static  Integer staticNum = 0; //类共享
    public Integer num = 0;//实例共享

    @PostMapping(value = "/addStatic")
    public void addStatic(){
        staticNum++;
        System.out.println("staticNum:"+staticNum);
    }

    @PostMapping(value = "/add")
    public void add(){
        num++;
        System.out.println("num:"+num);
    }

    @PostMapping(value = "/funAdd")
    public void funAdd(){
        Integer funNum = 0;
        funNum++;
        System.out.println("funNum:"+funNum);
    }
}
