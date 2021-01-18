package com.fosuchao.remoting.transport.netty.server;

import com.fosuchao.enums.CompressTypeEnum;
import com.fosuchao.enums.RpcResponseCodeEnum;
import com.fosuchao.enums.SerializationTypeEnum;
import com.fosuchao.factory.SingletonFactory;
import com.fosuchao.remoting.constants.RpcConstant;
import com.fosuchao.remoting.dto.RpcMessage;
import com.fosuchao.remoting.dto.RpcRequest;
import com.fosuchao.remoting.dto.RpcResponse;
import com.fosuchao.remoting.handler.RpcRequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chao Ye on 2021/1/16
 */
@Slf4j
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcMessage) {
            log.info("server接收到了msg：[{}]", msg);
            byte messageType = ((RpcMessage) msg).getMessageType();
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
            rpcMessage.setCompressType(CompressTypeEnum.GZIP.getCode());
            if (messageType == RpcConstant.HEARTBEAT_REQUEST_TYPE) {
                rpcMessage.setMessageType(RpcConstant.HEARTBEAT_RESPONSE_TYPE);
                rpcMessage.setData(RpcConstant.PONG);
            } else {
                rpcMessage.setMessageType(RpcConstant.RESPONSE_TYPE);
                RpcRequest rpcRequest = (RpcRequest) ((RpcMessage) msg).getData();
                // 调用方法
                Object result = rpcRequestHandler.handle(rpcRequest);
                log.info(String.format("server 执行结果: %s", result.toString()));
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                    rpcMessage.setData(rpcResponse);
                } else {
                    RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCodeEnum.FAIL);
                    rpcMessage.setData(rpcResponse);
                }
            }
            ctx.writeAndFlush(rpcMessage);
        }

    }
}
