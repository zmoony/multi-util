package nacos;

import com.sun.corba.se.impl.activation.CommandHandler;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * NacosProviderApplication
 * 通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
 *
 * @author yuez
 * @since 2023/4/10
 */
@SpringBootApplication
//@EnableDiscoveryClient
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
