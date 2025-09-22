package com.example.boot3.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * LoginController
 *
 * @author yuez
 * @since 2024/4/3
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/")
    public String login() {
        return "login";
    }
}
