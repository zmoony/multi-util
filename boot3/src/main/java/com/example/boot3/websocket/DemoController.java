package com.example.boot3.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DemoController
 *
 * @author yuez
 * @since 2024/3/27
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/ws/{cid}")
    public String toWsIndex(@PathVariable("cid") String cid, Model model){
        model.addAttribute("cid",cid);
        return "ws";
    }
}
