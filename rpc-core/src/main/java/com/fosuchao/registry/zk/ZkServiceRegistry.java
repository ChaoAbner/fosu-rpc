package com.fosuchao.registry.zk;

import com.fosuchao.registry.ServiceRegistry;
import com.fosuchao.registry.zk.utils.CuratorUtil;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * Created by Chao Ye on 2021/1/15
 * 服务注册
 */
public class ZkServiceRegistry implements ServiceRegistry {

    @Override
    public void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtil.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        CuratorUtil.createPersistentNode(zkClient, servicePath);
    }
}
