package com.boot.shiro.httpservice;

import com.boot.shiro.httpservice.openfeign.OpenFeignService;
import com.boot.shiro.httpservice.restTemplate.RestTemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

/**
 * StartHttpRunner
 *
 * @author yuez
 * @since 2024/11/27
 */
@Log4j2
@Component
@EnableFeignClients
@EnableHystrix
public class StartHttpRunner implements CommandLineRunner {
    @Autowired
    private OpenFeignService openFeignService;
    @Autowired
    private RestTemplateService restTemplateService;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("****************http start****************");
            String s = openFeignService.lockAdd();
            log.info("openFeignService:{}",s);
            String s1 = restTemplateService.restTemplateGet();
            log.info("restTemplateService:{}",s1);
        } catch (Exception e) {
            log.error("error:{}",e);
        }
    }
}
