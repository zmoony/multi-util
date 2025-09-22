package com.boot.util.retry;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * RetryFunction
 *
 * @author yuez
 * @since 2025/9/3
 */
@Log4j2
public class RetryFunction {
    private RetryFunction() {
    }

    /**
     * 通用重试方法（增强版）
     */
    public static boolean retryOperation(BooleanSupplier operation,
                                         RetryConfig config,
                                         String operationName) {
        for (int i = 0; i < config.getMaxRetries(); i++) {
            try {
                // 添加重试延迟（除了第一次）
                if (i > 0) {
                    long delay = config.getDelay(i);
                    log.info("第{}次重试{}，{}毫秒后重试", i + 1, operationName, delay);
                    SleepUtil.sleep(delay);
                } else {
                    log.info("第{}次执行{}", i + 1, operationName);
                }

                boolean result = operation.getAsBoolean();
                if (result) {
                    log.info("第{}次执行{}成功", i + 1, operationName);
                    return true;
                } else {
                    log.warn("第{}次执行{}失败", i + 1, operationName);
                }
            } catch (Exception e) {
                log.error("第{}次执行{}异常：{}", i + 1, operationName, e.getMessage());
                if (i == config.getMaxRetries() - 1) {
                    log.error("最后一次重试{}仍然失败：{}", operationName, e.getMessage(), e);
                }
            }
        }
        log.error("重试{}次后{}仍然失败", config.getMaxRetries(), operationName);
        return false;
    }


    /**
     * 发送数据重试 (普通版)
     */
    public static boolean sendWithRetry(BooleanSupplier operation, int retryNum) {
        for (int i = 0; i < retryNum; i++) {
            log.info("第{}次发送数据", i + 1);
            boolean sendData = operation.getAsBoolean();
            if (sendData) {
                log.info("第{}次重试发送数据成功", i + 1);
                return true;
            }
        }
        log.error("重试发送数据失败");
        return false;
    }
}
