package com.example.service;

import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * ServiceRegister
 * 模拟服务注册与发现
 * @author yuez
 * @since 2023/3/31
 */
public class ServiceRegisterAndDiscovery {

    public static void main(String[] args) throws Exception {
        ServiceRegisterAndDiscovery registerAndDiscovery = new ServiceRegisterAndDiscovery();
//        registerAndDiscovery.registerService();
        registerAndDiscovery.invokeService();
//        registerAndDiscovery.updateService("172.18.12.144:5050");
//        registerAndDiscovery.registerService();
    }

    private String service = "/user";
    private String serviceAddress = "localhost:8080";

    /**
     * 服务注册
     * @throws Exception
     */
    public void registerService() throws Exception {
       ServiceConfig.getInstance().addService(service,serviceAddress);
    }

    public void updateService(String value) throws Exception {
        ServiceConfig.getInstance().updateService(service,value);
    }

    public void invokeService() throws Exception {
        String url = "";
        if (!ServiceConfig.getInstance().existService(service)) {
            System.out.println("未能找到user服务，服务未注册或已下线");
        }else{
            url = ServiceConfig.getInstance().getAddress(service);
        }
        //监听
        ServiceConfig.getInstance().addListener(service, new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("服务变化了");
                invokeService();
            }
        });
        //添加获取监听
        System.out.println("向"+url+"发起请求");
    }




}
