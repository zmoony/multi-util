package com.boot.util.wiscom;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class RedisUtil {


    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
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
     * 放入String数据，带过期时间
     *
     * @param key
     * @param value
     */
    public static boolean setExStringValue(String key,String value,long time,TimeUnit timeUnit) {
        try {
            if (!StringUtils.isBlank(key)) {
                redisTemplate.opsForValue().set(key,value,time,timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
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
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
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
    public static List<Object> pipelineValue(List<Map<String, String>> list) {

        List<Object> redisResult = redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                RedisSerializer valueS = redisTemplate.getValueSerializer();
                for(Map<String,String> map:list){
                    String key = map.get("plateno");
                    redisConnection.get(key.getBytes());
                }
                return null;
            }
        }, redisTemplate.getValueSerializer());

        return redisResult;

    }








}
