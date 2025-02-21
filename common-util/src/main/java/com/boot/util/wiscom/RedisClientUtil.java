package com.boot.util.wiscom;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

/**
 * redis客户端
 *
 * @author hwang
 * @date 2022-01-04
 */
@Log4j2
public class RedisClientUtil {

    /**redis是否使用集群模式*/
    private static boolean isCluster = Boolean.valueOf(GlobalObject.properties_business_my.getProperty("redis.cluster.flag"));

    /**key过期时间 单位 秒*/
    private static long ExpireTime = Integer.parseInt(GlobalObject.properties_business_my.getProperty("redis.expire.time"))*3600;

    /**
     * redis同步执行的命令-单点模式
     */
    private static RedisStringCommands<String,String> redisCommands;
    /**
     * redis同步执行的命令-集群模式
     */
    private static RedisAdvancedClusterCommands<String, String> redisClusterCommands;

    /**
     * 初始化
     */
    public static void init() {
        String strPassword = GlobalObject.properties_business_my.getProperty("redis.password");
        String strIps = GlobalObject.properties_business_my.getProperty("redis.ips");

        if (isCluster) {
            ArrayList<RedisURI> list = new ArrayList<>();
            String[] listIps = strIps.split(",");
            for (String strIp : listIps) {
                list.add(RedisURI.create("redis://" + strPassword + "@" + strIp));
            }
            StatefulRedisClusterConnection statefulRedisClusterConnection = RedisClusterClient.create(list).connect();

            redisClusterCommands = statefulRedisClusterConnection.sync();

            log.info("redis {} 集群模式 连接初始化成功",strIps);
        } else {
            String[] listIps = strIps.split(",");
            String strIp = listIps[0];

            StatefulRedisConnection statefulRedisConnection = RedisClient.create("redis://" + strPassword + "@" + strIp).connect();

            redisCommands = statefulRedisConnection.sync();

            log.info("redis {} 单点模式 连接初始化成功",strIp);
        }

    }

    /**
     * 获取字符串值
     * @param strKey Key
     * @return String Value
     */
    public static String getValue(String strKey) {
        String strValue;
        if (isCluster) {
            strValue = redisClusterCommands.get(strKey);
        } else {
            strValue = redisCommands.get(strKey);
        }
        return  strValue;
    }
    /**
     * 设置字符串值
     * @param strKey Key
     * @param strValue Value
     * @return String Return
     */
    public static String setValue(String strKey,String strValue) {
        String strRet;
        if (isCluster) {
            strRet = redisClusterCommands.setex(strKey,ExpireTime,strValue);
        } else {
            strRet = redisCommands.setex(strKey,ExpireTime,strValue);
        }
        return  strRet;
    }
}
