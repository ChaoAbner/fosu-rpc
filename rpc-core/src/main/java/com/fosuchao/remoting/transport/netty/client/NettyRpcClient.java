package com.fosuchao.remoting.transport.netty.client;

import com.fosuchao.enums.CompressTypeEnum;
import com.fosuchao.enums.SerializationTypeEnum;
import com.fosuchao.extension.ExtensionLoader;
import com.fosuchao.factory.SingletonFactory;
import com.fosuchao.registry.ServiceDiscovery;
import com.fosuchao.registry.zk.ZkServiceDiscovery;
import com.fosuchao.remoting.constants.RpcConstant;
import com.fosuchao.remoting.dto.RpcMessage;
import com.fosuchao.remoting.dto.RpcRequest;
import com.fosuchao.remoting.dto.RpcResponse;
import com.fosuchao.remoting.transport.RpcRequestTransport;
import com.fosuchao.remoting.transport.netty.codec.RpcMessageDecoder;
import com.fosuchao.remoting.transport.netty.codec.RpcMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Chao Ye on 2021/1/18
 */
@Slf4j
public class NettyRpcClient implements RpcRequestTransport {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    private final ServiceDiscovery serviceDiscovery;

    public NettyRpcClient() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel.class)
                // 5s超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ZkServiceDiscovery.class).getExtension("zk");
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 连接Server，并且返回Channel，用于发rpc message给Server
     * @param inetSocketAddress     Server地址
     * @return                      Channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("client连接：[{}] 成功",inetSocketAddress.toString());
            } else {
                throw new IllegalArgumentException();
            }
        });
        return completableFuture.get();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 异步响应
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        // 服务发现，选择请求的服务器
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setData(rpcMessage);
            rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
            rpcMessage.setCompressType(CompressTypeEnum.GZIP.getCode());
            rpcMessage.setMessageType(RpcConstant.REQUEST_TYPE);
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("客户端发送消息：[{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("客户端发送消息失败：", future.cause());
                }
            });
        } else {
            throw new IllegalArgumentException();
        }
        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
