package com.boot.websocket.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

/**
 * CustomController
 *
 * @author yuez
 * @since 2024/12/3
 */
@Controller
public class CustomController {

    /**
     * 直接输出中文
     *
     * @param out
     * @param response
     * @throws IOException
     */
    @GetMapping("/output")
    public void output(OutputStream out, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        out.write("你好".getBytes());
    }

    /**
     * 自己处理请求body
     * @param is
     * @param response
     */
    @PostMapping("/input")
    public void inputDeal(InputStream is, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        StringBuilder buf = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(is, "UTF-8"); BufferedReader br = new BufferedReader(reader)) {
            String line = null;
            while ((line = br.readLine()) != null) {
                buf.append(line);
            }
            response.getWriter().write(buf.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 不受传参影响，自定义参数
     * @param version  常量字符串，输出redis.host
     * @param port 读取配置文件输出
     * @return
     */
    @GetMapping("/val")
    @ResponseBody
    public Object getFIxedParam (
            @Value("redis.host") String version,
            @Value("${redis.port}") String port,
            @Value("#{systemProperties['java.home']}") String spel){
        return version+":"+port+":"+spel;
    }

    /**
     * body 和 header一起获取
     * @param httpEntity
     * @return
     */
    @PostMapping("/entity")
    @ResponseBody
    public Object getHttpEntries(HttpEntity httpEntity) {
        System.out.printf("headers:%s%n",httpEntity.getHeaders());
        return httpEntity.getBody();
    }


}
