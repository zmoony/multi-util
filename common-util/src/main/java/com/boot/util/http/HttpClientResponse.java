package com.boot.util.http;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author yuez
 * @date 2022/06/08
 */
@Accessors(chain = true)
@Data
@ToString
public class HttpClientResponse {

    private HttpClientResponseStatus status;
    private String message;
    private Object data;

    private HttpClientResponse(HttpClientResponseStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static HttpClientResponse success() {
        return success(null);
    }

    public static HttpClientResponse success(Object data) {
        return new HttpClientResponse(HttpClientResponseStatus.success, null, data);
    }

    public static HttpClientResponse warn() {
        return warn(null);
    }

    public static HttpClientResponse warn(String message) {
        return new HttpClientResponse(HttpClientResponseStatus.warn, message, null);
    }

    public static HttpClientResponse error() {
        return error(null);
    }

    public static HttpClientResponse error(String message) {
        return new HttpClientResponse(HttpClientResponseStatus.error, message, null);
    }
}
