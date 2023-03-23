package com.boot.es.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
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
    public Docket createBasicIndexApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .host("http://localhost:9091/")
                .groupName("ES后台接口-索引")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.boot.es.controller.basic"))
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();

    }

    @Bean
    public Docket createDocApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .host("http://localhost:9091/")
                .groupName("ES后台接口-所有")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.boot.es.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("ES接口文档")
                .description("描述内容：elasticsearch接口文档，支持7.X以上版本")
                .contact(new Contact("yuez", "", ""))
                .termsOfServiceUrl("http://localhost:9091/")
                .version("1.0")
                .build();
    }
}
