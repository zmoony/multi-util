## docker
### 集成springboot
#### 1. Dockerfile 
```shell
FROM openjdk:11
LABEL maintainer="John Doe <john.doe@example.com>"
LABEL version="1.0"
LABEL description="My Spring Boot application"
ENV SPRING_PROFILES_ACTIVE=production
COPY target/my-application.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

```
#### 2. 使用 .dockerignore 文件
```bash
# 忽略根目录下的所有文件
*
# 包含 src 目录
!src/
# 包含 pom.xml 文件
!pom.xml
# 排除目标目录及其内容
target/
```