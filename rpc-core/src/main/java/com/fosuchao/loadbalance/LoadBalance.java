package com.fosuchao.loadbalance;

import java.util.List;

/**
 * Created by Chao Ye on 2021/1/15
 * 负载均衡
 */
public interface LoadBalance {

    /**
     * 负载均衡算法选择某个服务下的其中一个ip
     * @param serviceAddresses      ip集合
     * @param rpcServiceName        服务名称
     * @return
     */
    String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName);
}
