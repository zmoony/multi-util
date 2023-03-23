package com.boot.redis.pool;


import com.boot.redis.common.GlobalObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于Lettuce
 */
@Log4j2
@Configuration
public class RedisClusterConfig {

    @Autowired
    private GlobalObject globalObject;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        LettuceConnectionFactory lcf = null;
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

        if(GlobalObject.MODEL_SINGLE.equals(globalObject.model)){
            //单节点配置
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(globalObject.redisHost);
            redisStandaloneConfiguration.setPort(globalObject.redisPort);
            redisStandaloneConfiguration.setPassword(RedisPassword.of(globalObject.password));
            lcf=new LettuceConnectionFactory(redisStandaloneConfiguration,clientConfig);
        }else{
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
            lcf=new LettuceConnectionFactory(redisClusterConfiguration,clientConfig);
        }
        return lcf;
    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisConnection redisConnection(RedisTemplate redisTemplate){
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        return redisConnection;
    }

    /**
     * 绑定消息监听者和接收监听的方法,必须要注入这个监听器，不然会报错
     */
   /* @Bean
    public MessageListenerAdapter listenerAdapter(){
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage
        return new MessageListenerAdapter(new MessageReceiver(),"receiveMessage");
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter,new PatternTopic(SystemConstants.TOPIC_NAME));
        return container;
    }*/



}

