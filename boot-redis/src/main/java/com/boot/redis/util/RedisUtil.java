package com.boot.redis.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis的工具类
 */
@Component
@Log4j2
public class RedisUtil {


    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static String ping(){
        return redisTemplate.getRequiredConnectionFactory().getConnection().ping();
    }

    /**
     * 以map形式存储所有字段
     *
     * @param key
     * @param objMap
     */
    public static boolean setMapAllField(String key, Map<String, String> objMap) {
        try {
            if (!StringUtils.isBlank(key)) {
                redisTemplate.opsForHash().putAll(key, objMap);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }


    /**
     * 存储和修改map中的某个字段
     *
     * @param key
     * @param fieldName
     * @param fieldValue
     */
    public static boolean setMapField(String key, String fieldName, String fieldValue) {
        try {
            if (!StringUtils.isBlank(key)) {
                redisTemplate.opsForHash().put(key, fieldName, fieldValue);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }


    /**
     * 自增
     * @param key
     * @param fieldName
     * @return
     */
    public static boolean crbyMapField(String key, String fieldName){
        try {
            if (!StringUtils.isBlank(key)) {
                redisTemplate.opsForHash().increment(key,fieldName,1);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 根据key获取整个map
     *
     * @param key
     * @return
     */
    public static Map<Object, Object> getMapAllField(String key) {
        Map<Object, Object> objMap = new HashMap<>();
        try {
            if (!StringUtils.isBlank(key)) {
                objMap = redisTemplate.opsForHash().entries(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return objMap;
    }

    /**
     * 根据key和字段名获取map中的某一个字段
     *
     * @param key
     * @param fieldName
     * @return
     */
    public static String getMapField(String key, String fieldName) {
        String result = "";
        try {
            if (!StringUtils.isBlank(key)) {
                result = redisTemplate.opsForHash().get(key, fieldName) + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return result;
    }

    public static String getStringValue(String key) {
       String str = null;
        try {
            if (!StringUtils.isBlank(key)) {
                str = redisTemplate.opsForValue().get(key)+"";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return str;
    }



    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }


    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 批量存储
     */
    public static void pipelineInsert(List<Map<String, Object>> list) {
        redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                RedisSerializer valueS = redisTemplate.getHashValueSerializer();
                for(Map<String,Object> map:list){
                    String key = map.get("plateno").toString();
                    redisConnection.set(key.getBytes(),valueS.serialize(map));
                }
                return null;
            }
        });

    }


    /**
     * 批量获取
     */
    public static List<Map<Object,Object>> scan2(String hkey) {
        List<Map<Object,Object>> list = new ArrayList<>();
        List<String> redisResult = redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                List<String> str = new ArrayList<>();
                Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions().match(hkey).count(200000).build());
                while(cursor.hasNext()){
                    str.add(new String(cursor.next()));
                }
                return str;
            }
        });
        log.info("获取key:{}",redisResult);
        for (String key : redisResult) {
            Map<Object, Object> mapAllField = new HashMap<>();
            Map<Object, Object> map = getMapAllField(key);
            String keyp = key.substring(key.indexOf("=") + 1);

            list.add(mapAllField);
        }
        return list;

    }




    /**
     * scan获取--hashkey
     */
    public static Map<Object,Object> scanMatchHashKey(String hkey,String hashkey){
        Map<Object,Object> map=new HashMap<>();
        try {
            String key = "tongji=*"+hkey+"*";
            ScanOptions scanOptions = ScanOptions.scanOptions().match("*"+hashkey+"*").count(1000).build();
            Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash().scan(
                    key,scanOptions);
            while (cursor.hasNext()){
                Map.Entry<Object,Object> entry = cursor.next();
                map.put(entry.getKey(),entry.getValue());
            }
//            map.clear();
            cursor.close();
        } catch (IOException e) {
            log.error("获取hashkey出错{}",e);
        }
        return map;
    }



}
