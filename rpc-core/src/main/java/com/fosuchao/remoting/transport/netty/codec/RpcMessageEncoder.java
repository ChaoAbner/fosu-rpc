package com.fosuchao.remoting.transport.netty.codec;

import com.fosuchao.remoting.dto.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by Chao Ye on 2021/1/16
 */
public class RpcMessageEncoder extends MessageToMessageDecoder<RpcMessage> {

    @Override
    protected void decode(ChannelHandlerContext ctx, RpcMessage msg, List<Object> out) throws Exception {

    }
}
