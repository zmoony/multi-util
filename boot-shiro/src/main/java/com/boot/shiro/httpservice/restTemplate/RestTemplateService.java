package com.boot.shiro.httpservice.restTemplate;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateService
 *
 * @author yuez
 * @since 2024/11/27
 */
@Service
@Log4j2
public class RestTemplateService {
    @Autowired
    private RestTemplate restTemplate;

    public String restTemplateGet() {
        return restTemplate.postForObject("http://localhost:9099/lock/add2", null, String.class);
    }
}
