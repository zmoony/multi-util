# 注意事项
## pom
```properties
 <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <!-- 在SpringCloud 2020.* 版本把bootstrap禁用了，导致在读取文件的时候读取不到而报错，
        所以我们只要把bootstrap从新导入进来就会生效了。 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
            <version>3.0.3</version>
        </dependency>
```
## 配置文件
需要新建bootstrap.properties
```properties
spring.application.name=service-provider
spring.cloud.nacos.config.server-addr=172.18.12.144:8848
spring.cloud.nacos.config.file-extension=properties
spring.cloud.nacos.config.group=cloud-test
#共享配置(共享配置文件,即多个应用配置中有共同的配置项，如redis,mysql,hystrix等等，这些公共配置，我们可以抽象出来，通过nacos的shared-configs来共享。)
spring.cloud.nacos.config.shared-configs[0].data-id=service-provider-common.properties
spring.cloud.nacos.config.shared-configs[0].refresh=true
#导入配置文件（继承配置）--spring.config.import属性,用来导入本地或者远程的公用配置文件
spring.config.import=optional:nacos:service-provider-common.properties

spring.cloud.nacos.discovery.server-addr=${spring.cloud.nacos.config.server-addr}
#spring.cloud.nacos.discovery.namespace=db489f97-82cf-4b7e-b39f-f1604d7a3120
```
原来的application.properties
```properties
server.port=8086
spring.profiles.active=test
server.tomcat.basedir=/log
server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.pattern='%t %a "%r" %s %S (%b M) (%D ms)'
```
## java
### 服务发现
@EnableDiscoveryClient
```java
@SpringBootApplication
@EnableDiscoveryClient
@Log4j2
public class NacosProviderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("88888");
        System.out.println("00000");
    }
}
```
### 配置动态管理
```java
@RestController
@RefreshScope
public class ConfigController {
    /**
     * 直接使用
     */
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    /**
     * 配置文件
     */
    @Value("${oracle.server}")
    private String server;
    @Value("${oracle.host}")
    private String host;
    @Value("${oracle.port}")
    private String port;
    @Value("${oracle.user}")
    private String user;
    @Value("${oracle.password}")
    private String password;

    @RequestMapping("/get")
    public boolean get() {
        return useLocalCache;
    }

    @RequestMapping("/get2")
    public String get2() {
        return Arrays.asList(server,host,port,user,password).stream().collect(Collectors.joining(";"));
    }
}
```
## gateway
### pom
```xml
    <dependencies>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!--  网关-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!-- 负载均衡 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
        </dependency>
    </dependencies>
```
### 配置文件
```yaml
server:
  port: 8090
spring:
  application:
    name: nacos-gateway
  cloud:
    nacos:
      discovery:
        server-addr: 172.18.12.144:8848
    gateway:
      discovery:
        locator:
          enabled: true #表明gateway开启服务注册和发现的功能，并且spring cloud gateway自动根据服务发现为每一个服务创建了一个router，这个router将以服务名开头的请求路径转发到对应的服务
          lower-case-service-id: true #是将请求路径上的服务名配置为小写（因为服务注册的时候，向注册中心注册时将服务名转成大写的了
      routes:
        - id: service-provider
          uri: lb://service-provider
          predicates:
            - Path=/p/**
          filters:
            - StripPrefix=1
      enabled: true
```

### 启动类
```java
@SpringBootApplication
@EnableDiscoveryClient
public class NacosGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosGatewayApplication.class, args);
    }
}
```

### 跨域问题
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```
