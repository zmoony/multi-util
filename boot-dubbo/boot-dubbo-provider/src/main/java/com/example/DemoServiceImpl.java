package com.example;

import org.apache.dubbo.config.annotation.DubboService;

/**
 * DemoServiceImpl
 * {@code  @DubboService}发布dubbo服务
 *
 * @author yuez
 * @since 2023/3/30
 */
@DubboService(group = "group1",version = "1.0")
public class DemoServiceImpl implements IDemoService{
    @Override
    public String sayHello(String name) {
        return "dubbo "+name;
    }
}
