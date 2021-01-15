package com.fosuchao.registry;

import java.net.InetSocketAddress;

/**
 * Created by Chao Ye on 2021/1/15
 */
public interface ServiceDiscovery {

    /**
     * 服务发现
     * @param rpcServiceName    服务的名称
     * @return                  服务的调用地址
     */
    InetSocketAddress lookupService(String rpcServiceName);
}
