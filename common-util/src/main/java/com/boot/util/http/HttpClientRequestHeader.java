package com.boot.util.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yuez
 * @date 2022/06/08
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class HttpClientRequestHeader {
    /**
     * 请求头key，例如：Content-Type
     */
    private String key;
    /**
     * 请求头value：例如：application/json
     */
    private String value;
}
