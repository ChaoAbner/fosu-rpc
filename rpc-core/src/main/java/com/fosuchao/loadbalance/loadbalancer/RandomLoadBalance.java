package com.fosuchao.loadbalance.loadbalancer;

import com.fosuchao.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * Created by Chao Ye on 2021/1/15
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
