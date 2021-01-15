package com.fosuchao.loadbalance;

import java.util.List;

/**
 * Created by Chao Ye on 2021/1/15
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcServiceName);
    }

    protected abstract String doSelect(List<String> serviceAddresses, String rpcServiceName);
}
