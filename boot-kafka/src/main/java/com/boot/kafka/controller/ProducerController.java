package com.boot.kafka.controller;

import com.boot.kafka.common.R;
import com.boot.kafka.common.RequestData;
import com.boot.kafka.dao.ProducerDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者的控制层
 *
 * @author yuez
 * @version 1.0.0
 * @className ProducerController
 * @date 2021/3/29 13:51
 **/
@Api(value = "生产者操作集合",tags = "生产者操作集合")
@Log4j2
@RestController
@RequestMapping("producer")
public class ProducerController {
    @Autowired
    private ProducerDao producerDao;

    @ApiOperation(value = "等幂模式发送数据",notes = "无法保证跨分区和跨会话的会话",produces = "application/json")
    @PostMapping("/send/idem")
    public R sendBathMessageIdem(@RequestBody RequestData requestData){
        boolean b = producerDao.sendBathMessageIdem(requestData.getData(), requestData.getTopic());
        return R.ok().code(b?200:500).message(b?"发送成功":"发送失败");
    }

    @ApiOperation(value = "事务模式发送数据",notes = "消费端记得设置isolation.level",produces = "application/json")
    @PostMapping("/send/tran")
    public R sendBathMessageTran(@RequestBody RequestData requestData){
        boolean b = producerDao.sendBathMessageTran(requestData.getData(), requestData.getTopic());
        return R.ok().code(b?200:500).message(b?"发送成功":"发送失败");
    }

}
