package com.boot.util.wiscom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object转换为List
 * @author hwang
 */
public class ObjectToListUtil {
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    public static <K, V> List<Map<K, V>> castListMap(Object obj, Class<K> kCalzz, Class<V> vCalzz) {
        List<Map<K, V>> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object mapObj : (List<?>) obj) {
                if (mapObj instanceof Map<?, ?>) {
                    Map<K, V> map = new HashMap<>(16);
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) mapObj).entrySet()) {
                        map.put(kCalzz.cast(entry.getKey()), vCalzz.cast(entry.getValue()));
                    }
                    result.add(map);
                }
            }
            return result;
        }
        return null;
    }
}
