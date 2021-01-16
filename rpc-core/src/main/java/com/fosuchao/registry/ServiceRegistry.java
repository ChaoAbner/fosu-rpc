package com.fosuchao.registry;

import com.fosuchao.extension.SPI;

import java.net.InetSocketAddress;

/**
 * Created by Chao Ye on 2021/1/15
 */
@SPI
public interface ServiceRegistry {

    /**
     * 注册服务
     * @param rpcServiceName        服务注册名称
     * @param inetSocketAddress     服务调用地址
     */
    void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
