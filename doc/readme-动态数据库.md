## pom
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>
        <!--阿里druid数据库链接依赖-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.6</version>
</dependency>
```
## 配置文件
```yaml
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
```
## 使用
```java
String getMinCJBHWs();

@DS(DataSourceEnum.WS)
String getMinCJBHWs();
```