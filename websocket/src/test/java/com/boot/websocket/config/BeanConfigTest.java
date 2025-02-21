package com.boot.websocket.config;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes ={BeanPropertiesConfig.class, BeanYamlConfig.class} )
class BeanConfigTest {

    @Autowired
    private BeanPropertiesConfig beanPropertiesConfig;

    @Autowired
    private BeanYamlConfig beanYamlConfig;

    @org.junit.jupiter.api.Test
    void test() throws Exception{
        System.out.println(beanPropertiesConfig);
    }

    @org.junit.jupiter.api.Test
    void test2() throws Exception{
        System.out.println(beanYamlConfig);
    }

}
