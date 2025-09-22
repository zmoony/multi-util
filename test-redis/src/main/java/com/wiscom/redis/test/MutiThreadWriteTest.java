package com.wiscom.redis.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiscom.redis.pool.RedisPool;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MutiThreadWriteTest
 *
 * @author yuez
 * @since 2023/11/22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class MutiThreadWriteTest {
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
                final String value = LocalDateTime.now().toString();
                for (int j = 0; j < 1000000; j++) {
                    commands.set("test:"+j, value);
                    log.info("threadname:"+Thread.currentThread().getName()+" set test:"+j+":{}", value);
                    assert value.equals(value);
                }
            }).start();
        }
    }


    @Test
    public void testList() throws JsonProcessingException {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> map = new HashMap<>();
        map.put("jcbk_id","32102020_1");
        map.put("id","0");
        map.put("status","1");
        String v1 = objectMapper.writeValueAsString(map);

        Map<String,Object> map1 = new HashMap<>(16);
        map1.put("jcbk_id","32102020_2");
        map1.put("id","1");
        map1.put("status","1");
        String v2 = objectMapper.writeValueAsString(map1);

        commands.lpush("test:list:1", v1, v2);
    }

    @Test
    public void testList2() throws JsonProcessingException {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        List lrange = commands.lrange("test:list:2", 0, -1);
        System.out.println(lrange);
    }

    @Test
    public void testHash() throws JsonProcessingException {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();

        Map<String,Object> map = new HashMap<>();
        map.put("jcbk_id","32102020_1");
        map.put("id","0");
        map.put("status","1");

        System.out.println(commands.hmset("test:hash:1",map));
    }

    @Test
    public void testHashGet() throws JsonProcessingException {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();

        System.out.println(commands.hgetall("test:hash:1"));
    }

    @Test
    public void testUpdateHash() throws JsonProcessingException {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();

        System.out.println(commands.hset("test:hash:1","status","100"));
    }

    @Test
    public void testClear() throws JsonProcessingException {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();

        System.out.println(commands.flushdb());
    }
    @Test
    public void testLength() {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        System.out.println(commands.dbsize());
    }


    @Test
    public void testKey() {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        System.out.println(commands.keys("*"));
        System.out.println(commands.keys("t*"));
        System.out.println(commands.keys("y*"));
    }

    /**
     * 不支持通配符
     */
    @Test
    public void testExist() {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        System.out.println(commands.exists("test:hash:1"));
    }

    /**
     * 不支持通配符
     */
    @Test
    public void testDelete() {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        System.out.println(commands.del("test:vvvv:1"));
    }

    @Test
    public void testUpdateList() {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        List<String> allKeys = commands.keys("*");
        List mget = commands.mget(allKeys.toArray(new String[0]));
        System.out.println(mget);
    }

    @Test
    public void testGetString() {
        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        List<String> allKeys = commands.keys("*");
        List<KeyValue> mget = commands.mget(allKeys.toArray(new String[0]));
        System.out.println(mget);

        /**
         * keys会有性能问题，使用scan
         */

        scanValues(commands);


    }

    private void scanValues(RedisCommands commands) {
        ScanArgs scanArgs = new ScanArgs().match("hash:*");
        KeyScanCursor keyScanCursor = commands.scan(scanArgs);
        List<String> keys = keyScanCursor.getKeys();
        System.out.println(keys);
        for (String key : keys) {
            System.out.println(commands.hgetall(key));
        }
    }


    @Test
    public void testGetCustom() {
//        saveRedis("time:11",LocalDateTime.now(),RedisPool.statefulRedisConnectionW);
        LocalDateTime localDateTime = readRedis("time:12", RedisPool.statefulRedisConnectionW);
        assert localDateTime == null;
    }

    /**
     * key没有会自动添加
     */
    @Test
    public void testHsetCustom() {
       /* Map<String,Object> map = new HashMap<>();
        map.put("count","1");
        map.put("time",LocalDateTime.now().toString());
        saveHashRedis("hash:m:12",map,RedisPool.statefulRedisConnectionW);*/

        RedisCommands commands = RedisPool.statefulRedisConnectionW.sync();
        commands.hincrby("hash:m:12","count",1);
        commands.hset("hash:m:12","time",LocalDateTime.now().toString());

        commands.hincrby("hash:m:10","count",1);
        commands.hset("hash:m:10","time",LocalDateTime.now().toString());
    }


    public  void saveRedis(String key,LocalDateTime value, StatefulRedisConnection connection){
        RedisCommands redisCommands = connection.sync();
        String format = value.format(timeFormatter);
        redisCommands.set(key,format);
    }
    public  LocalDateTime readRedis(String key, StatefulRedisConnection connection){

        if(StringUtils.isNotBlank(key)){
            RedisCommands redisCommands = connection.sync();
            Object value = redisCommands.get(key);
            if(value == null){
                return null;
            }
            String s =value  + "";
            return LocalDateTime.parse(s,timeFormatter);
        }else {
            log.error("KEY为空");
            return null;
        }
    }

    public static void saveHashRedis(String key, Map<String,Object> map, StatefulRedisConnection connection){
        try {
            RedisCommands redisCommands = connection.sync();
            redisCommands.hmset(key,map);
        } catch (Exception e) {
            log.error("保存redis-hash操作：{}",e);
        }
    }
}
