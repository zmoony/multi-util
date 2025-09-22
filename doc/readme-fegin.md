# fegin
1. 声明式的REST客户端
2. 继承了Ribbon(负载均衡)和Hystrix（断路器）
3. 注解支持
4. 动态代理
5. 请求和响应的处理
6. 易于扩展

## @FeignClient注解工作原理
@FeignClient 注解的工作原理是在运行时动态创建带注解接口的代理。
1. 获取客户端名称
2. 获取configuration属性
3. 注册客户端