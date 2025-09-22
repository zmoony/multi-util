# nacos
http://172.18.12.144:8848/nacos 账户/密码：nacos/nacos
## docker安装
1. 在线模式
- derby模式
```shell
git clone https://github.com/nacos-group/nacos-docker.git
cd nacos-docker
-- 单机模式 derby
docker-compose -f example/standalone-derby.yaml up -d
[root@localhost example]# docker-compose -f standalone-derby.yaml up -d
/usr/local/lib/python3.6/site-packages/paramiko/transport.py:33: CryptographyDeprecationWarning: Python 3.6 is no lo
cryptography and will be removed in a future release.
  from cryptography.hazmat.backends import default_backend
Starting nacos-standalone ... done
Starting grafana          ... done
Starting prometheus       ... Done
```
- 其他启动模式
```shell
----单机模式 MySQL5.7
docker-compose -f example/standalone-mysql-5.7.yaml up
----单机模式 MySQL8
docker-compose -f example/standalone-mysql-8.yaml up
---集群模式
docker-compose -f example/cluster-hostname.yaml up 
---服务注册
curl -X POST 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=nacos.naming.serviceName&ip=20.18.7.10&port=8080'
---服务发现
curl -X GET 'http://127.0.0.1:8848/nacos/v1/ns/instance/list?serviceName=nacos.naming.serviceName'
--发布配置
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test&content=helloWorld"
--获取配置
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test&content=helloWorld"
```
- 常见问题
```shell
--问题1
ERROR: manifest for nacos/nacos-server:test not found: manifest unknown: manifest unknown
打开.evn文件，把版本号2.0.4改成v2.2.0
[root@localhost example]# cat .env
NACOS_VERSION=v2.2.0
```
## nacos springboot
- 通过 Nacos Server 和 nacos-config-spring-boot-starter 实现配置的动态变更；
- 通过 Nacos Server 和 nacos-discovery-spring-boot-starter 实现服务的注册与发现。
### 1. 配置管理
####  1.1 pom
```xml
<!--nacos https://mvnrepository.com/artifact/com.alibaba.boot/nacos-config-spring-boot-starter-->
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-config-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```
####  1.2 配置
```properties
nacos.config.bootstrap.enable=true
# 主配置服务器地址
nacos.config.server-addr=101.35.99.141:8848
nacos.config.username=nacos
nacos.config.password=nacos
# 主配置 data-id
nacos.config.data-id=test
# 主配置 group-id 即配置分组，用于较广的分类，通常使用工程名，表示其下配置属于指定工程。一个group下可以有多个配置集，且不同的group下可以存在同id的配置集。
nacos.config.group=boot-nacos-yuez1
# 主配置 配置文件类型
nacos.config.type=properties
# 主配置 最大重试次数
nacos.config.max-retry=10
# 主配置 开启自动刷新
nacos.config.auto-refresh=true
# 主配置 重试时间
nacos.config.config-retry-time=2333
# 主配置 配置监听长轮询超时时间
nacos.config.config-long-poll-timeout=46000
# 主配置 开启注册监听器预加载配置服务（除非特殊业务需求，否则不推荐打开该参数）
nacos.config.enable-remote-sync-config=true
#允许nacos上的配置优先于本地配置
nacos.config.remote-first=true
```
#### 1.3 配置项两种（直接引用/导入配置文件）
#####    1.  直接引用
所谓直接引用就是在SpringBoot中直接以注解形式获取nacos中配置集的配置项值。
1. 使用 @NacosPropertySource 注解在XXXApplication中加载dataId为test的配置源，并开启自动更新：
```java
@SpringBootApplication
@NacosPropertySource(dataId = "test",autoRefreshed = true)
public class BootNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootNacosApplication.class, args);
    }

}
```
2. 通过Nacos的@NacosValue注解获取属性值。
```java
@Controller
@RequestMapping("config")
public class ConfigController {
    @NacosValue(value = "${testValue:false}",autoRefreshed = true)
    private boolean testValue;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public boolean get(){
        return testValue;
    }

}
```
即将nacos中test配置集下的配置项testValue赋给了ConfigController的成员变量testValue，从而可以在该类任何方法中使用。
3. 测试
>发送请求POST：http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=test&group=boot-nacos-yuez1&content=testValue=abc123
>获取配置：http://localhost:8080/getTestValue

#####    2. 导入配置文件
导入配置即将nacos中的指定配置集导入到.properties中，就像这些配置是被定义在.properties中的一样。使用时，直接从.properties中加载指定配置项即可。
1. 配置到配置文件
```properties
#导入引用
nacos.config.ext-config[0].data-id=test2
nacos.config.ext-config[0].group=boot-nacos-yuez1
nacos.config.ext-config[0].auto-refresh=true
nacos.config.ext-config[0].max-retry=10
nacos.config.ext-config[0].type=yaml
nacos.config.ext-config[0].config-retry-time=2333
nacos.config.ext-config[0].config-long-poll-timeout=46000
nacos.config.ext-config[0].enable-remote-sync-config=true
```
2. 从properties中加载配置
```java
@CrossOrigin
@RestController
public class ConfigController {
    @Autowired
    private Environment env;

    @RequestMapping("/getTestValue")
    public String getTestValue() {
        return env.getProperty("testValue");
    }
}

```

### 2. 服务注册与发现
#### 2.1 pom
```xml
<!--nacos-->
<dependency>
  <groupId>com.alibaba.boot</groupId>
  <artifactId>nacos-discovery-spring-boot-starter</artifactId>
  <version>${latest.version}</version>
</dependency>

```


#### 2.2 配置
```properties
nacos.discovery.server-addr=${nacos.config.server-addr}
nacos.discovery.auto-register=true
```

#### 2.3 服务端
```java
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


@Controller
@RequestMapping("server")
public class  ServerController {

    @RequestMapping(value = "/get")
    @ResponseBody
    public String get(String serviceName) throws NacosException {
        return "the server receives the message :" + serviceName;
    }
}
```
#### 2.3 客户端
```java
@CrossOrigin
@RestController
public class ClientController {
    @Autowired
    private RestTemplate restTemplate;
    @NacosInjected
    private NamingService namingService;

    String serviceName = "boot-nacos-yuez1";
    String groupName = "test";
    String api = "/server/get";

    /**
     * get方式传参调用nacos服务
     */
    @RequestMapping("/getClientValueByGet")
    public String getClientValueByGet(String message) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance(serviceName,groupName);
        String url = "http://" + instance.getIp() + ":" + instance.getPort() + api + "?serviceName=" + message;
        System.out.println("请求地址："+url);
        return restTemplate.getForObject(url,String.class);
    }

    /**
     * post方式传参调用nacos服务
     */
    @RequestMapping("/getClientValueByPost")
    public String getClientValueByPost(String message) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance(serviceName, groupName);
        String url = "http://" + instance.getIp() + ":" + instance.getPort() + api;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("serviceName",message);
        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        System.out.println("请求地址："+url);
        return restTemplate.postForObject(url,httpEntity,String.class);
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

```
## nacos springcloud

































