package com.example.text.demoOnLine.认证与授权;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/hello")
  public String hello() {
    return "hello,hresh";
  }

  @GetMapping("/hresh")
  public String sayHello() {
    return "hello,world";
  }

  @PostMapping("/doLogin")
  public String doLogin() {
    return "我登录成功了";
  }

  @GetMapping(value = "/r/r1")
  public String r1() {
    return " 访问资源1";
  }

  @GetMapping(value = "/r/r2")
  public String r2() {
    return " 访问资源2";
  }
}