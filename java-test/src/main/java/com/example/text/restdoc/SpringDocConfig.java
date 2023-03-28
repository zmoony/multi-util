package com.example.text.restdoc;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author yuez
 * @since 2022/6/17
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI mallTinyOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("hello API")
                        .description("演示API")
                        .version("1.0.0")
                        .license(new License().name("APache 2.0")
                                .url("https://github.com/macrozheng/mall-learning")))
                .externalDocs(new ExternalDocumentation()
                        .description("测试项目")
                        .url("https://www.nowcoder.com/discuss/968248?type=2&order=0&pos=29&page=0&channel=-1&source_id=discuss_center_2_nctrack"));
    }
    @Bean
    public GroupedOpenApi publicAPI(){
        return GroupedOpenApi.builder()
                .group("brand")
                .pathsToMatch("/*")
                .build();
    }
    @Bean
    public GroupedOpenApi adminAPI(){
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
