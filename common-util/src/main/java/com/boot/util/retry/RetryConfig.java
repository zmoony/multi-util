package com.boot.util.retry;

import lombok.Data;

/**
 * @author yuez
 */
@Data
public class RetryConfig {
    private final int maxRetries;
    private final long baseDelayMs;
    private final boolean exponentialBackoff;

    public RetryConfig(int maxRetries, long baseDelayMs, boolean exponentialBackoff) {
        this.maxRetries = maxRetries;
        this.baseDelayMs = baseDelayMs;
        this.exponentialBackoff = exponentialBackoff;
    }

    public long getDelay(int retryCount) {
        if (exponentialBackoff) {
            return baseDelayMs * (1L << (retryCount - 1)); // 指数退避
        } else {
            return baseDelayMs * retryCount; // 线性退避
        }
    }
}
