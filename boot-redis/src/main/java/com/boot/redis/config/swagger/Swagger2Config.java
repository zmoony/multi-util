package com.boot.redis.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yuez
 * @title: Swagger2Config
 * @projectName zy_testProject
 * @description: Swagger2Config
 * @date 2021/3/3 9:44
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("redis接口文档")
                .description("描述内容：redis接口文档")
                .contact(new Contact("yuez", "", ""))
                .termsOfServiceUrl("http://localhost:9099/")
                .version("1.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .host("http://localhost:9099/")
                .groupName("kafka后台接口")
                .apiInfo(apiInfo)
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.boot.es.controller.basic"))
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();

    }
}
