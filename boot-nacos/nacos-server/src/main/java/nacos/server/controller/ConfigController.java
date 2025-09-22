package nacos.server.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 通过 Nacos 的 @NacosValue 注解设置属性值
 *
 * @author yuez
 * @since 2023/2/23
 */
@Controller
@RequestMapping("config")
public class ConfigController {

    /**
     * 直接引用
     * 现在项目里给默认值，后面配置文件在页面修改
     */
    @NacosValue(value = "${testValue:false}",autoRefreshed = true)
    private boolean testValue;

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ResponseBody
    public boolean get(){
        return testValue;
    }

    /**
     * 导入引用
     */
    @Autowired
    private Environment env;

    @RequestMapping("/getTestValue")
    public String getTestValue(){
        return env.getProperty("testValue");
    }




}
