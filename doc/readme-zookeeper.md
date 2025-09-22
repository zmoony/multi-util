# boot整合zookeeper
## 1. 搭建zookeeper
1.1 docker
```agsl
docker search zookeeper
docker pull zookeeper
docker images
docker inspect zookeeper
--启动容器
docker run -d --name zookeeper --privileged=true -p 2181:2181 -v /usr/local/yuez/software/zookeeper/data/:/data  -v /usr/local/yuez/software/zookeeper/logs/:/datalog --restart always zookeeper:latest
```
1.2 直接安装
```agsl
wget https://mirrors.bfsu.edu.cn/apache/zookeeper/zookeeper-3.6.2/apache-zookeeper-3.6.2-bin.tar.gz
tar -zxvf apache-zookeeper-3.6.2-bin.tar.gz
cd apache-zookeeper-3.6.2-bin

# 前台启动
bin/zkServer.sh start-foreground
```
## 2. maven
```xml
<!-- https://mvnrepository.com/artifact/org.apache.zookeeper/zookeeper -->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.7.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
## 3. 基础知识
zk的数据模型和我们常见的目录树很像，从/开始，每一个层级就是一个节点（数据 + 子节点）
**`节点类型一经指定，不允许修改
临时节点，当会话结束，会自动删除，且不能有子节点`**
> 1. 持久节点 persistent node
> 2. 持久顺序节点 persistent sequental
> 3. 临时节点 ephemeral node
> 4. 临时顺序节点 ephemeral sequental

## 4. 操作注意点
- 节点需要一个个层级创建
- 更改数据可以设置版本号（-1为只要节点路径对上就成）
- 据版本限定删除， -1 表示不需要管版本，path匹配就可以执行；否则需要版本匹配，不然就会抛异常
- 需要手动创建监听，自带的监听只会触发一次

## 5. curator(推荐)
### 5.1 maven
```xml
 <!-- zookeeper -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.7.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- zookeeper 框架  curator-->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>5.2.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>5.2.1</version>
        </dependency>

        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-client</artifactId>
            <version>5.2.1</version>
        </dependency>
```
## 6 功能应用
### 6.1 服务注册与发现
- 先建父节点services
- 注册服务：根据服务名（users）在下面新建子节点，value为地址
- 服务发现：根据服务名（users）获取value,新建监听，动态获取value
- zookeeper会自动剔除下线的服务（心跳检测/节点类型为临时序列（子节点放value））

























