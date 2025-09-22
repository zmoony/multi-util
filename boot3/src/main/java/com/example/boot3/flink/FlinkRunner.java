package com.example.boot3.flink;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * FlinkRunner
 *
 * @author yuez
 * @since 2025/5/21
 */
@Component
@AllArgsConstructor
public class FlinkRunner implements CommandLineRunner {
    private final FlinkJob flinkJob;

    @Override
    public void run(String... args) throws Exception {
        flinkJob.run();
    }
}
