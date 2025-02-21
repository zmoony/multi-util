package com.boot.shiro.httpservice.restTemplate;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * CustomErrorHandler
 *
 * @author yuez
 * @since 2024/11/27
 */
@Log4j2
public class CustomErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // 判断响应是否有错误，例如 4xx 或 5xx
        return (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
// 自定义错误处理逻辑
        if (response.getStatusCode().is4xxClientError()) {
            // 处理 4xx 错误
            log.error("404 Client Error: " + response.getStatusText());
        } else if (response.getStatusCode().is5xxServerError()) {
            // 处理 5xx 错误
            log.error("500 Server Error: " + response.getStatusText());
        }
    }
}
