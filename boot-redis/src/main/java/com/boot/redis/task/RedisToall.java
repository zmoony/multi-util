/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.redis.task;


import com.boot.redis.util.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;

import java.util.List;
import java.util.Map;

/**
 * @author yuez
 * @DisallowConcurrentExecution //不允许多实例运行，确保唯一性
 */
@Log4j2
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class RedisToall implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        startGetRedis();
    }

    private void startGetRedis() {
        List<Map<Object, Object>> entries = RedisUtil.scan2(getKey());
        if(entries==null){
            log.error("获取key出错");
            return;
        }
        if(entries.size()==0){
            //更新时间
            log.info("设置当前时间为：{}");
            return;
        }
        //发送服务，发送成功更新时间
    }

    private String getKey(){
        return null;
    }

}


