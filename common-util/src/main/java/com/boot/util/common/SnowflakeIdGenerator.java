package com.boot.util.common;

/**
 * SnowflakeIdGenerator
 * 雪花计算器
 * 1 位符号位，始终为0，用于标识正数 | 41位时间戳 | 10位机器标识 | 12位序列号
 *
 * @author yuez
 * @since 2024/12/27
 */
public class SnowflakeIdGenerator {
    //定义雪花ID的各部分位数
    private static final long TIMESTAMP_BITS = 41L;
    private static final long NODE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    //定义起始时间戳
    private static final long START_TIMESTAMP = 1640995200000L;
    //定义最大取值范围
    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;
    //定义时间戳左移位数
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + NODE_ID_BITS;
    private static final long NODE_ID_LEFT_SHIFT = SEQUENCE_BITS;

    private long lastTimestamp = -1L;
    private long sequence = 0L;
    private  long nodeId;

    public SnowflakeIdGenerator(long nodeId) {
        if (nodeId > MAX_NODE_ID || nodeId < 0) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + MAX_NODE_ID);
        }
        this.nodeId = nodeId;
    }

    /**
     * 1.防止始终回拨，产生相同的ID
     *
     * 2.如果当前时间戳与上次相同，则递增序列号。
     * 如果序列号达到最大值，则等待直到下一个毫秒再生成ID。
     * 如果时间戳不同，则重置序列号为0。
     *
     *
     * @return
     */
    public synchronized long generateId() {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        } else if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = currentTimestamp;
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT) | (nodeId << NODE_ID_LEFT_SHIFT) | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }


    public static void main(String[] args) {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1);
        System.out.println(System.nanoTime());
        for (int i = 0; i < 10; i++) {
            long id = generator.generateId();
            System.out.println("Generated ID: " + id);
        }
    }
}
