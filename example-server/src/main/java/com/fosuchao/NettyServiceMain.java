package com.fosuchao;

import com.fosuchao.annotation.RpcScan;
import com.fosuchao.entity.RpcServiceProperties;
import com.fosuchao.remoting.transport.netty.server.NettyRpcServer;
import com.fosuchao.service.impl.HelloServiceImpl2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Chao Ye on 2021/2/19
 */
@RpcScan(basePackage = {"com.fosuchao"})
public class NettyServiceMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServiceMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        // 注册服务
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceProperties rpcServiceProperties =
                RpcServiceProperties.builder().group("test2").version("version2").build();
        nettyRpcServer.registerService(helloService2, rpcServiceProperties);
        nettyRpcServer.start();

    }
}
