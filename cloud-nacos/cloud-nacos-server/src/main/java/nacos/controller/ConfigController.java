package nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * ConfigController
 * 通过 Spring Cloud 原生注解 @RefreshScope 实现配置自动更新
 * @author yuez
 * @since 2023/4/10
 */
@RestController
@RefreshScope
public class ConfigController {
    /**
     * 直接使用
     */
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    /**
     * 共享配置
     */
    @Value("${common.username}")
    private String username;

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

    @RequestMapping("/get3")
    public String get3() {
        return username;
    }

    public static void main(String[] args) {
        String inserTime = "1700115445686";
        LocalDateTime insertLocalDateTime = Instant.ofEpochMilli(Long.parseLong(inserTime)).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
}
