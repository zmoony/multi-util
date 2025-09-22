package com.boot.websocket.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HuaGongYuanGatewayTest
 *
 * @author yuez
 * @since 2025/4/18
 */
@RestController
@RequestMapping("/keshihua")
public class HuaGongYuanGatewayTest {

    @PostMapping("/pushLoadAndUnload")
    public String test() {
        return "pushLoadAndUnload";
    }

    @PostMapping("/pushEnterAndOutInfo")
    public String test2() {
        return "pushEnterAndOutInfo";
    }
}
