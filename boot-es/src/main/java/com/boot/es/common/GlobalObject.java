package com.boot.es.common;

import com.boot.es.config.ESConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 定义全局变量
 *
 * @author yuez
 * @version 1.0.0
 * @className GlobalObject
 * @date 2021/3/13 15:16
 **/
@Service
@Log4j2
public class GlobalObject {

    @Autowired
    private ESConfig esConfig;

    public static ESConfig ES_INFO;

    @PostConstruct
    public void init(){
        ES_INFO = esConfig;
        log.info("elasticsearch信息初始化成功");
    }
}
