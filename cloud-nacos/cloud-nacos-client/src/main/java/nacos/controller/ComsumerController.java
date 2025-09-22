package nacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * ComsumerController
 *
 * @author yuez
 * @since 2023/4/10
 */
@RestController
public class ComsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

  /*  @Autowired
    private LoadBalancerClient loadBalancerClient;*/

    @GetMapping("/echo/{string}")
    public String echo(@PathVariable String string){
        List<ServiceInstance> instances = discoveryClient.getInstances("service-provider");
        ServiceInstance serviceInstance = instances.get(0);
        URI uri = serviceInstance.getUri();
        return restTemplate.getForObject(uri+"/echo/"+string,String.class);
    }
}
