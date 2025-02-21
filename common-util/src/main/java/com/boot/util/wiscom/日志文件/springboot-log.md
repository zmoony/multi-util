# pom
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--包含log4j2 启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
            <version>2.6.2</version>
        </dependency>
```

# 配置文件
```properties
logging.config = config/log4j2.xml
```

# log4j2.xml
放在config下面
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="error" monitorInterval="30">
     <Properties>
<!--       <Property name="LOG_FILE">log/psmp_common_alltostk.log</Property>-->
        <Property name="CONSOLE_LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n</Property>
         <!--定义log路径-->
         <Property name="logDir">log</Property>
         <!--定义log文件名-->
         <Property name="logName">psmp_common_alltostk</Property>
         <Property name="FILE_LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n</Property>
    </Properties>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT" follow="true">
            <PatternLayout pattern="${CONSOLE_LOG_PATTERN}"/>
        </Console>
        <RollingFile name="RollingFile" fileName="${logDir}/${logName}.log"
                     filePattern="${logDir}/$${date:yyyy-MM}/${logName}_%d{MM-dd-yyyy}-%i.log.gz">
            <PatternLayout pattern="${FILE_LOG_PATTERN}"/>
            <Policies>
                <!--触发日志rollover的时间单位，1表示一个时间单位-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <!--触发日志rollover的长度单位，250m表示超过这个文件大小则触发rollover-->
                <SizeBasedTriggeringPolicy size="20m"/>
            </Policies>
            <!--每日log文件最多20个-->
            <DefaultRolloverStrategy max="200">
                <!--maxDepth表示扫描路径深度，2为basePath下一级目录文件-->
                <Delete basePath="${logDir}/" maxDepth="2">
                    <IfFileName glob="${logName}_*.log.gz" />
                    <!-- 日志保留策略，配置只保留十五天 -->
                    <IfLastModified age="15d" />
                </Delete>
            </DefaultRolloverStrategy>

        </RollingFile>

    </Appenders>

    <Loggers>
        <Root level="info">
            <AppenderRef  ref="Console"/>
            <AppenderRef  ref="RollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```