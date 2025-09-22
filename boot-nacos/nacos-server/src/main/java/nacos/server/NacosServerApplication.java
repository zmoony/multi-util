package nacos.server;

import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NacosServerApplication
 *
 * @author yuez
 * @since 2023/4/4
 */
@SpringBootApplication
//@NacosPropertySource(dataId = "test",autoRefreshed = true)
@EnableNacosDiscovery
public class NacosServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosServerApplication.class, args);
    }
}
