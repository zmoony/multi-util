package com.boot.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuez
 * @title: LoginController
 * @projectName muti-scaffold
 * @description: 登录控制器
 * @date 2022/5/6 16:24
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        try {
            // 执行登录
            subject.login(token);
            return "ok";
        } catch (UnknownAccountException e) {
            return e.getMessage();
        } catch (IncorrectCredentialsException e) {
            return "IncorrectCredentialsException " + e.getMessage();
        } catch (LockedAccountException e) {
            return "LockedAccountException " + e.getMessage();
        } catch (AuthenticationException e) {
            return "认证失败！";
        }
    }
}
