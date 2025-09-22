package com.example.service;

import com.example.curator.CuratorUtil;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * ServiceInit
 * 服务初始化
 *
 * @author yuez
 * @since 2023/3/31
 */
public class ServiceConfig {
    public static final String path = "/services";
    public static CuratorUtil curatorUtil = new CuratorUtil("");
    private static ServiceConfig serviceConfig = new ServiceConfig();

    public static ServiceConfig getInstance() {
        return serviceConfig;
    }

    ;

    /**
     * 初始化父节点
     *
     * @throws Exception
     */
    private ServiceConfig() {
        try {
            if (!curatorUtil.existNode(path)) {
                curatorUtil.createNode(path, "");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addService(String serviceName, String address) throws Exception {
        if(!existService(ServiceConfig.path + serviceName)){
            curatorUtil.createNode(ServiceConfig.path + serviceName, address);
        }
    }

    public boolean existService(String serviceName) throws Exception {
        return curatorUtil.existNode(ServiceConfig.path + serviceName);
    }

    public String getAddress(String serviceName) throws Exception {
        return ServiceConfig.curatorUtil.getNodeData(ServiceConfig.path + serviceName);
    }

    public void addListener(String serviceName, NodeCacheListener listener) throws Exception {
        ServiceConfig.curatorUtil.addWatcherWithNodeCache(ServiceConfig.path + serviceName,listener);
    }


    public void updateService(String service, String value) throws Exception {
        curatorUtil.setNodeData(ServiceConfig.path + service,value);
    }
}
