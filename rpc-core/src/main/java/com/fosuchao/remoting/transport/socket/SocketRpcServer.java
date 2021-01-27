package com.fosuchao.remoting.transport.socket;

import com.fosuchao.config.CustomShutdownHook;
import com.fosuchao.entity.RpcServiceProperties;
import com.fosuchao.factory.SingletonFactory;
import com.fosuchao.provider.ServiceProvider;
import com.fosuchao.provider.ServiceProviderImpl;
import com.fosuchao.remoting.transport.netty.server.NettyRpcServer;
import com.fosuchao.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Created by Chao Ye on 2021/1/27
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;

    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public void registerService(Object service) {
        serviceProvider.publishService(service);
    }

    public void registerService(Object service, RpcServiceProperties properties) {
        serviceProvider.publishService(service, properties);
    }

    public void start() {
        try {
            ServerSocket server = new ServerSocket();
            String address = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(address,  NettyRpcServer.PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("客户端连接：[{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("创建socket发生IO异常：", e);
        }
    }

}
