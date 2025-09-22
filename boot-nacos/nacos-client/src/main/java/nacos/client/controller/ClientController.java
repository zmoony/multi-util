package nacos.client.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * ClientController
 * 服务调用
 *
 * @author yuez
 * @since 2023/4/3
 */
@CrossOrigin
@RestController
public class ClientController {
    @Autowired
    private RestTemplate restTemplate;
    @NacosInjected
    private NamingService namingService;

    String serviceName = "boot-nacos-yuez1";
    String groupName = "test";
    String api = "/server/get";

    /**
     * get方式传参调用nacos服务
     */
    @RequestMapping("/getClientValueByGet")
    public String getClientValueByGet(String message) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance(serviceName,groupName);
        String url = "http://" + instance.getIp() + ":" + instance.getPort() + api + "?serviceName=" + message;
        System.out.println("请求地址："+url);
        return restTemplate.getForObject(url,String.class);
    }

    /**
     * post方式传参调用nacos服务
     */
    @RequestMapping("/getClientValueByPost")
    public String getClientValueByPost(String message) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance(serviceName, groupName);
        String url = "http://" + instance.getIp() + ":" + instance.getPort() + api;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("serviceName",message);
        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        System.out.println("请求地址："+url);
        return restTemplate.postForObject(url,httpEntity,String.class);
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
