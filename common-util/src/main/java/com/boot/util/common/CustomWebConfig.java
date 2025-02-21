package com.boot.util.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CustomWebConfig implements WebMvcConfigurer {

    // 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义拦截器
//       registry.addInterceptor(new MyCustomInterceptor());
    }

    // 跨域资源共享（CORS）配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许所有源访问
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
                .allowCredentials(true) // 是否允许携带凭证（如cookies）
                .maxAge(3600); // 预检请求的有效期
    }

    // 注册默认的页面跳转控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index"); // 访问根路径时重定向到"index"视图
    }

    // 静态资源处理
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/"); // 设置静态资源文件夹位置
    }

    // 其他可能需要定制的配置方法，例如：
    // 自定义视图解析器、消息转换器、内容协商策略等
}
