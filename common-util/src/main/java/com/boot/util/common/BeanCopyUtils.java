package com.boot.util.common;

import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * cglib的beanCopier，开销在创建BeanCopier，一般在创建类的时候提前创建好一个，在代码运行的时候直接进行copy，性能接近原生。
 *
 * @author yuez
 * @since 2022/9/14
 */
public class BeanCopyUtils {
    /**
     * beanCopier缓存
     * 由sourceClass和targetClass可以确定一个唯一的BeanCoper，因此使用二级Map；
     */
    private static Map<Class<?>, Map<Class<?>, BeanCopier>> beanCopierMap = new ConcurrentHashMap<>();

    /**
     * 直接指定Bean对象进行拷贝
     * @param sourceBean
     * @param targetBean
     * @param <S>
     * @param <T>
     */
    public static <S,T> void copy(S sourceBean,T targetBean){
        @SuppressWarnings("unchecked")
        Class<S> sourceClass = (Class<S>) sourceBean.getClass();
        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) targetBean.getClass();

        BeanCopier beanCopier = getBeanCopier(sourceClass,targetClass);
        beanCopier.copy(sourceBean,targetBean,null);

    }
    /**
     * 转换方法
     * @param sourceBean 原对象
     * @param targetClass 目标类
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S,T> T convert(S sourceBean,Class<T> targetClass){
        try {
            assert sourceBean!=null;
            T targetBean = targetClass.newInstance();
            copy(sourceBean,targetBean);
            return targetBean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    private static <S, T> BeanCopier getBeanCopier(Class<S> sourceClass, Class<T> targetClass) {
        Map<Class<?>, BeanCopier> map = beanCopierMap.get(sourceClass);
        if(map == null ||map.isEmpty()){
            BeanCopier beanCopier = BeanCopier.create(sourceClass, targetClass, false);
            Map<Class<?>,BeanCopier> newMap = new ConcurrentHashMap<>();
            newMap.put(targetClass,beanCopier);
            beanCopierMap.put(sourceClass,newMap);
            return beanCopier;
        }
        BeanCopier beanCopier = map.get(targetClass);
        if(beanCopier == null){
            BeanCopier newBeanCopier = BeanCopier.create(sourceClass, targetClass, false);
            map.put(targetClass,newBeanCopier);
            return newBeanCopier;
        }
        return beanCopier;
    }


}
