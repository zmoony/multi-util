package com.example.boot3.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * curl -X "GET" "localhost:9210/test/sayHello?name=name_vh6af" \
 *并不知道你到底是路径传参还是问号传参：
 * querystring 方式传递参数（例如 "/user?name=cyk" ）：在 openfeign 接口声明中必须要给参数加入 @RequestParam 注解/
 * restful 路径传参（例如 "/user/{id}/{name}" ）：在 openfeign 接口声明中必须要给参数加入注解 @PathVariable 注解.
 *
 * 对象传参 @RequestBody
 * openfeign 中对象传参只能使用 POST，并且也符合使用习惯，最主要是因为 GET 请求传对象会报错 Method Not Allowed.
 *
 * 数组传参
 * （@RequestParam("arr") String[] arr）
 *
 * @author yuez
 */
@FeignClient(name = "hello", url = "http://localhost:9210")
public interface HelloClient {
    /**
     * 必须有@RequestParam，否则参数传递不过去
     * @param name
     * @return
     */
    @GetMapping("/test/sayHello")
    String sayHello(@RequestParam("name") String name);

    /**
     * 必须有@PathVariable，否则参数传递不过去
     * @param name
     * @return
     */
    @GetMapping("/test/{name}")
    String sayHello2(@PathVariable("name") String name);


}
