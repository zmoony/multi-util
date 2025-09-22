## 配置文件加载顺序
1. **命令行参数**：通过 -- 开头的命令行参数传递，例如
```bash
java -jar myapp.jar --server.port=8081
```
2. **Java系统属性**：通过 -D 选项设置 Java 系统属性，例如：
```bash
   java -Dserver.port=8081 -jar myapp.jar
```
3. **环境变量**：通过环境变量设置，例如：
```bash
   export SERVER_PORT=8081
```
4. **配置文件**：通过命令行参数 --spring.config.location 设置配置文件的路径，例如：
```bash
   java -jar myapp.jar --spring.config.location=/opt/myapp/config/
```
5. **类路径**：如果在类路径下存在 application.yml，则加载它
```properties
Spring Boot加载配置文件的顺序如下：

1. config/application.properties 或 config/application.yml （在项目根目录下的config文件夹内）。
2. application.properties 或 application.yml （在项目根目录下）。
3. resources/config/application.properties 或 resources/config/application.yml （在项目的resources目录下的config文件夹内）。
4. resources/application.properties 或 resources/application.yml （在项目的resources目录下）。
此外，Spring Boot还会自动加载jar包内的配置文件，优先级由高到低为：./config/application.properties 或 ./config/application.yml、./application.properties 或 ./application.yml、classpath:/config/application.properties 或 classpath:/config/application.yml。

如果同一个配置属性在多个配置文件中都配置了，那么会使用第一次读取到的值，后面的配置文件中的该属性值不会被覆盖。
```


## 在最外层的config里配置application.yml
```yml
datasource:
  #维数基础数据库
  ws:
    username: zhzx_dp
    password: zhzx_dp
    #url: 50.144.10.13:1521/orcl
    url: 172.17.112.253:1521/orcl
  ck:
    username: default
    password: wiscom123!
    #url: 50.144.18.125:8123/wiscom
    url: 172.17.112.123:8123/wiscom_yc
```
## resource下配置application.yml
```yml
#logback日志配置
logging:
  file:
    max-size: 50MB
    max-history: 7
    name: logs/datasync.log
    path: ./logs
  level:
    com:
      wiscom:
        kingcds:
          repository: debug #打印sql
#DB
spring:
  #多数据源配置
  datasource:
    dynamic:
      primary: ck
      strict: false #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源
      datasource:
        #维数基础数据库
        ws:
          username: ${datasource.ws.username}
          password: ${datasource.ws.password}
          driver-class-name: oracle.jdbc.OracleDriver
          url: jdbc:oracle:thin:@${datasource.ws.url}
        #clickhouse数据库，存储警情、案件、人脸预警、车辆预警
        ck:
          username: ${datasource.ck.username}
          password: ${datasource.ck.password}
          driver-class-name: ru.yandex.clickhouse.ClickHouseDriver
          url: jdbc:clickhouse://${datasource.ck.url}
#mybatis-plus配置
mybatis-plus:
  typeAliasesPackage: com.wiscom.repository
  mapper-locations: classpath:mapper/**/*Mapper.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl #开启sql日志


```