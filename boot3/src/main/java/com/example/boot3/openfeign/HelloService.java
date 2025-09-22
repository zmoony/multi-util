package com.example.boot3.openfeign;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloService
 *
 * @author yuez
 * @since 2024/1/12
 */
@RestController
public class HelloService {
    final HelloClient helloClient ;

    public HelloService(HelloClient helloClient) {
        this.helloClient = helloClient;
    }

    @GetMapping("/hello")
    public String hello(String name)
    {
        return helloClient.sayHello(name);
    }

    @GetMapping("/hello2")
    public String hello2(String name)
    {
        return helloClient.sayHello2(name);
    }
}
