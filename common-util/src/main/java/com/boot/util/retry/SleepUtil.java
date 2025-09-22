package com.boot.util.retry;

/**
 * SleepUtil
 *
 * @author yuez
 * @since 2025/9/3
 */
public class SleepUtil {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
