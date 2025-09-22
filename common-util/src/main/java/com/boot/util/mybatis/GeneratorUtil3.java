//package com.example.backend.util;
//
//import com.baomidou.mybatisplus.generator.FastAutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.OutputFile;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//
//import java.util.Collections;
//
///**
// * <pre>
// *     <dependency>
// *             <groupId>com.baomidou</groupId>
// *             <artifactId>mybatis-plus-boot-starter</artifactId>
// *             <version>3.5.5</version>
// *             <exclusions>
// *                 <exclusion>
// *                     <groupId>org.mybatis</groupId>
// *                     <artifactId>mybatis-spring</artifactId>
// *                 </exclusion>
// *             </exclusions>
// *         </dependency>
// *         <dependency>
// *             <groupId>org.mybatis</groupId>
// *             <artifactId>mybatis-spring</artifactId>
// *             <version>3.0.3</version>
// *         </dependency>
// *
// *         <dependency>
// *             <groupId>com.baomidou</groupId>
// *             <artifactId>mybatis-plus-generator</artifactId>
// *             <version>3.5.1</version>
// *         </dependency>
// *
// *         <!-- FreeMarker 引擎支持 -->
// *         <dependency>
// *             <groupId>org.freemarker</groupId>
// *             <artifactId>freemarker</artifactId>
// *             <version>2.3.31</version>
// *         </dependency>
// *
// *
// * </pre>
// */
//public class GeneratorUtil {
//
//    public static void main(String[] args) {
//        String projectPath = System.getProperty("user.dir") + "/case/code-generate";
//        System.out.println(projectPath);
//        FastAutoGenerator.create(new DataSourceConfig.Builder("jdbc:postgresql://172.17.112.121:9442/wiscomdb?useSSL=false",
//                        "wiscom", "Wiscom123!").schema("case_visualization"))
//                .globalConfig(builder -> builder.author("")
//                        .enableSwagger()
//                        .outputDir(projectPath + "/src/main/java"))
//                .packageConfig(builder -> {
//                    builder.parent("com.example.backend")
//                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper"));
//                })
//                .strategyConfig(builder -> builder.addInclude("case_handling_record","case_info","criminal_history"
//                        ,"mental_illness_history","person_relationship","police_record","suspect","suspect_case_relation",
//                        "victim","victim_case_relation",
//                        "dict_data").addTablePrefix(""))
//                .templateEngine(new FreemarkerTemplateEngine())
//                .execute();
//    }
//
//}
