package com.example.boot3.server;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试server
 *
 * @author yuez
 * @since 2023/2/17
 */
@RestController
@RequestMapping("/hello")

public class HelloController {

    @RequestMapping("/sayHello")
    @CrossOrigin
    public String sayHello(String name, HttpServletRequest request) {
        request.getSession().setAttribute("name", name);
        return "Hello " + name;
    }


}
