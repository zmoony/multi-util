package nacos.server.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.spring.util.Tuple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ServerRegisterConfig 服务注册的配置类
 * 使用 @NacosInjected 注入 Nacos 的 NamingService 实例：
 * @author yuez
 * @since 2023/4/3
 */
@Configuration
public class ServerRegisterConfig implements CommandLineRunner {
    @NacosInjected
    private NamingService namingService;

    @Value("${nacos.server.discovery.serverName}")
    private String serverName;
    @Value("${nacos.server.discovery.group-name}")
    private String groupName;
    @Value("${nacos.server.discovery.weight}")
    private Double weight;
    @Value("${server.port}")
    private int port;


    public void registerInstance() throws NacosException, UnknownHostException {
        Tuple<String,Integer> url = IpUtil.getUrlByInetAddress(port);
        Instance instance = new Instance();
        instance.setIp(url.getFirst());
        instance.setPort(url.getSecond());
        instance.setHealthy(true);
        instance.setWeight(weight);
        namingService.registerInstance(serverName,groupName,instance);
    }

    @Override
    public void run(String... args) throws Exception {
        registerInstance();
    }
}
