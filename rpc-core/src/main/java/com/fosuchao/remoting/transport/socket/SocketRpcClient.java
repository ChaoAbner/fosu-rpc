package com.fosuchao.remoting.transport.socket;

import com.fosuchao.entity.RpcServiceProperties;
import com.fosuchao.exception.RpcException;
import com.fosuchao.extension.ExtensionLoader;
import com.fosuchao.factory.SingletonFactory;
import com.fosuchao.registry.ServiceDiscovery;
import com.fosuchao.remoting.dto.RpcRequest;
import com.fosuchao.remoting.transport.RpcRequestTransport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Chao Ye on 2021/1/27
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RpcRequestTransport {

    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 获取serviceName
        String rpcServiceName = RpcServiceProperties.builder().group(rpcRequest.getGroup())
                .serviceName(rpcRequest.getInterfaceName()).version(rpcRequest.getVersion()).build().toRpcServiceName();
        InetSocketAddress address = serviceDiscovery.lookupService(rpcServiceName);
        // 通过输出流发送数据
        try {
            Socket socket = new Socket();
            socket.connect(address);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 发送数据到服务端
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 读取rpc响应
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw new RpcException("服务调用失败：", e);
        }
    }
}
