package com.boot.util.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @author yuez
 * @since 2022/6/9
 */
public class CacheUtil<K,V> {
    private static CacheUtil cacheUtil = new CacheUtil();
    public CacheUtil(){
        cache = Caffeine.newBuilder()
                .expireAfterWrite(12
                        , TimeUnit.HOURS)
                .maximumSize(1000000L)
                .build();
    }
    public static CacheUtil getInstance(){return cacheUtil;}

    private Cache<K, V> cache;
    /**
     * 返回所有keu值
     */
    public Set<K> getKeys() {
        return cache.asMap().keySet();
    }

    public void remove(K key) {
        cache.invalidate(key);
    }
    public void removeAll() {
        cache.asMap().clear();
    }
    /**
     * 返回所有value值
     */
    public Collection<V> getValues() {
        return cache.asMap().values();
    }
    /**
     * 返回entrySet
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return cache.asMap().entrySet();
    }
    /**
     * 获得某个字典值,没有则返回null
     *
     * @param key key值
     * @return key对应的value，没有则返回null
     */
    public V get(K key) {
        return cache.getIfPresent(key);
    }
    /**
     * 获得某个字典值,null则返回指定值
     *
     * @param key key值
     * @return key对应的value
     */
    public V get(K key, V defaultValue) {
        return Optional.ofNullable(cache.getIfPresent(key)).orElse(defaultValue);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void putAll(Map<K, V> datas) {
        cache.putAll(datas);
    }

    public int size(){
        return cache.asMap().size();
    }

}
