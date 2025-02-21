package com.boot.websocket.start;

import com.boot.websocket.config.BeanPropertiesConfig;
import com.boot.websocket.config.CommonConfig;
import com.boot.websocket.util.CommonCryptoUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * StartRunner
 *
 * @author yuez
 * @since 2024/11/26
 */
@Component
public class StartRunner implements CommandLineRunner {

    private BeanPropertiesConfig beanPropertiesConfig;
    private CommonConfig commonConfig;

    private CommonCryptoUtil commonCryptoUtil;

    public StartRunner(BeanPropertiesConfig beanPropertiesConfig, CommonConfig commonConfig, CommonCryptoUtil commonCryptoUtil) {
        this.beanPropertiesConfig = beanPropertiesConfig;
        this.commonConfig = commonConfig;
        this.commonCryptoUtil = commonCryptoUtil;
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("beanConfig:"+beanPropertiesConfig);
        System.out.println("commonConfig:"+commonConfig);
        System.out.println("commonCryptoUtil:"+commonCryptoUtil.aesCrypto("hello world"));
    }
}
