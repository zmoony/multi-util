package nacos.server.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 使用 @NacosInjected 注入 Nacos 的 NamingService 实例：
 * 服务发现
 * @author yuez
 * @since 2023/2/23
 */
@Controller
@RequestMapping("server")
public class  ServerController {

    @RequestMapping(value = "/get")
    @ResponseBody
    public String get(String serviceName) throws NacosException {
        return "the server receives the message :" + serviceName;
    }
}
