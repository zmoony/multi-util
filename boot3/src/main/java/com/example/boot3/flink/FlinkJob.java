package com.example.boot3.flink;

import org.springframework.stereotype.Component;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * FlinkJob
 *
 * @author yuez
 * @since 2025/5/21
 */
@Component
public class FlinkJob {

    public void run() throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.fromElements(1, 2, 3, 4, 5)
//                .map(x -> x + 1)
//                .print();

        env.fromElements("Hello", "Flink", "in", "Spring Boot")
                .map(String::toUpperCase)
                .print();
        env.execute("local flink spring boot test");
    }
}
