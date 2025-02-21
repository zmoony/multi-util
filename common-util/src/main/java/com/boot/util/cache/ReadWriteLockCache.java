package com.boot.util.cache;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLockCache
 * 使用读写锁控制缓存
 * 读写锁，读锁不释放，写锁就一直获取不到
 * @author yuez
 * @since 2024/8/28
 */
public class ReadWriteLockCache<K,V> {
    private final Map<K, V> m = new HashMap<>();
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    /**
     * 构造方法
     * 全量缓存--加载全局字典
     */
    public ReadWriteLockCache(){
        //查询数据库
        List<Map<K,V>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.parallelStream().forEach((f) ->{
//                m.put(f., f.getV);
            });
        }
    }

    public V get(K key) {
        r.lock();
        try {
            return m.get(key);
        } finally {
            r.unlock();
        }
    }

    public V put(K key, V value) {
        w.lock();
        try {
            return m.put(key, value);
        } finally {
            w.unlock();
        }
    }
}
