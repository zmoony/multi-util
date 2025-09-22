package com.boot.util.字典转换.转code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictItem {
    private Object myCode;      // 我方值
    private String thirdCode;   // 第三方值
    private String desc;        // 描述
}
