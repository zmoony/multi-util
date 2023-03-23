package com.example.text.java;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 消息实时推送
 * <li>guava-multiMap 一个key对应多个map</li>
 * <li>spring-MultiValueMap 一个key对应多个map</li>
 * <li>spring-DeferredResult <br>
 * 1. 浏览器发起异步请求<br>
 * 2. 请求到达服务端被挂起<br>
 * 3. 向浏览器进行响应，分为两种情况：<br>
 *  3.1 调用DeferredResult.setResult()，请求被唤醒，返回结果<br>
 *  3.2 超时，返回一个你设定的结果<br>
 * 4. 浏览得到响应，再次重复1，处理此次响应结果</li>
 *
 *
 *
 *
 * @author yuez
 * @since 2022/7/22
 */
@Controller
public class PushOrPullMessageTest {
//    public static MultiValueMap<String, DeferredResult>
}
