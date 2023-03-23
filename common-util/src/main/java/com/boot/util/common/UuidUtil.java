package com.boot.util.common;

/**
 * @author yuez
 * @description: uuid工具类
 * @date 2022/4/5 10:01
 */
public class UuidUtil {
    private static UuidUtil uuidUtil = new UuidUtil();

    private UuidUtil() {
    }

    public static UuidUtil getInstance() {
        return uuidUtil;
    }

    private static String getNumberUuid() {
        int hashCode = java.util.UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        final String substring = String.format("%10d", hashCode).substring(0, 10);
        return substring;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(UuidUtil.getNumberUuid());
        }
    }

}
