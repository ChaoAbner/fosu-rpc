package com.fosuchao.registry.zk;

import com.fosuchao.enums.RpcErrorMessageEnum;
import com.fosuchao.exception.RpcException;
import com.fosuchao.extension.ExtensionLoader;
import com.fosuchao.loadbalance.LoadBalance;
import com.fosuchao.registry.ServiceDiscovery;
import com.fosuchao.registry.zk.utils.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by Chao Ye on 2021/1/15
 * 服务发现
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        // 通过SPI加载
        loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        List<String> serviceUrls = CuratorUtil.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // 负载均衡
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrls, rpcServiceName);
        log.info("服务地址获取成功：" + targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
