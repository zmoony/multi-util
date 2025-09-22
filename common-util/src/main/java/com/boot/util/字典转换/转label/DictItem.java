package com.boot.util.字典转换.转label;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 字典条目 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictItem {
    private String dictType;   // 字典类型，如 GENDER、ORDER_STATUS
    private String value;      // 字典值
    private String label;      // 展示文本
    private Integer sort;
    private String locale;     // 预留：语言
    private Long tenantId;     // 预留：租户
}
