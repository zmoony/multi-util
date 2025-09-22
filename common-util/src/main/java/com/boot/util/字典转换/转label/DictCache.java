package com.boot.util.字典转换.转label;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DictCache {

    private final DictMapper dictMapper;      // MyBatis-Plus / JPA 均可

    private final Cache<String, Map<String, String>> cache =
            Caffeine.newBuilder()
                    .maximumSize(200)
                    .expireAfterWrite(Duration.ofMinutes(30))
                    .build();

    /** 启动时加载全部字典 。可以是配置文件*/
    @PostConstruct
    public void loadAll() {
        List<DictItem> items = dictMapper.selectList(null); // 可按租户/语言过滤
        Map<String, List<DictItem>> grouped =
                items.stream().collect(Collectors.groupingBy(DictItem::getDictType));

        grouped.forEach((type, list) -> {
            Map<String, String> kv = list.stream()
                    .collect(Collectors.toMap(DictItem::getValue,
                                              DictItem::getLabel,
                                              (v1, v2) -> v2));
            cache.put(type, kv);
        });
    }

    /** 运行时热刷新，对外暴露 /actuator/dict/refresh 端点即可 */
    public void refresh() {
        cache.invalidateAll();
        loadAll();
    }

    /** 读缓存 */
    public Optional<String> label(String dictType, String value) {
        return Optional.ofNullable(cache.getIfPresent(dictType))
                       .map(m -> m.get(value));
    }
}
