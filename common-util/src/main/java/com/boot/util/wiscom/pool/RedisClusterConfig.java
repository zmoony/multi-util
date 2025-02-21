package com.boot.util.wiscom.pool;


import com.boot.util.wiscom.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Configuration
public class RedisClusterConfig {

    @Autowired
    private GlobalObject globalObject;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){

        //单节点配置
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(globalObject.redisHost);
        redisStandaloneConfiguration.setPort(globalObject.redisPort);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(globalObject.password));
        if(globalObject.redisDataBase!=-1){
            redisStandaloneConfiguration.setDatabase(globalObject.redisDataBase);
        }
        //集群配置
        RedisClusterConfiguration redisClusterConfiguration=new RedisClusterConfiguration();
        String[] cNodes = globalObject.clusterNodes.split(",");
        List<RedisNode> nodeList = new ArrayList<>();
        for(String node : cNodes) {
            String[] hp = node.split(":");
            nodeList.add(new RedisNode(hp[0], Integer.parseInt(hp[1])));
        }
        redisClusterConfiguration.setClusterNodes(nodeList);
        redisClusterConfiguration.setPassword(RedisPassword.of(globalObject.password));
        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(globalObject.maxIdle);
        genericObjectPoolConfig.setMaxTotal(globalObject.maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(globalObject.maxWait);
        genericObjectPoolConfig.setTestOnBorrow(globalObject.testOnBorrow);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(globalObject.minEvictableIdleTimeMillis);
        genericObjectPoolConfig.setNumTestsPerEvictionRun(globalObject.numTestsPerEvictionRun);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(globalObject.timeBetweenEvictionRunsMillis);
        genericObjectPoolConfig.setSoftMinEvictableIdleTimeMillis(globalObject.minEvictableIdleTimeMillis);
        genericObjectPoolConfig.setBlockWhenExhausted(globalObject.blockWhenExhausted);


        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(globalObject.timeout))//连接超时时间
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lcf;
        if(globalObject.redisClientWay==0){
            lcf=new LettuceConnectionFactory(redisStandaloneConfiguration,clientConfig);
        }else{
            lcf=new LettuceConnectionFactory(redisClusterConfiguration,clientConfig);
        }
        return lcf;
    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(redisSerializer);
//        redisTemplate.setHashKeySerializer(redisSerializer);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}

