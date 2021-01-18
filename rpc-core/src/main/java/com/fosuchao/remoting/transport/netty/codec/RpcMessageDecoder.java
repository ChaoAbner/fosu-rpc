package com.fosuchao.remoting.transport.netty.codec;

import com.fosuchao.compress.Compress;
import com.fosuchao.enums.CompressTypeEnum;
import com.fosuchao.enums.SerializationTypeEnum;
import com.fosuchao.extension.ExtensionLoader;
import com.fosuchao.remoting.constants.RpcConstant;
import com.fosuchao.remoting.dto.RpcMessage;
import com.fosuchao.remoting.dto.RpcRequest;
import com.fosuchao.remoting.dto.RpcResponse;
import com.fosuchao.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * custom protocol decoder
 *
 *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *   |                                                                                                       |
 *   |                                         body                                                          |
 *   |                                                                                                       |
 *   |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 * body（object类型数据）
 *
 * Created by Chao Ye on 2021/1/16
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        // lengthFieldOffset: magic code is 4B, and version is 1B, and then full length. so value is 5
        // lengthFieldLength: full length is 4B. so value is 4
        // lengthAdjustment: full length include all data and read 9 bytes before, so the left length is (fullLength-9). so values is -9
        // initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
        this(RpcConstant.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) decoded;
            if (buf.readableBytes() >= RpcConstant.MAX_FRAME_LENGTH) {
                try {
                    return decodeFrame(buf);
                } catch (Exception e) {
                    log.error("解码错误", e);
                } finally {
                    buf.release();
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        int magicLen = RpcConstant.MAGIC_NUMBER.length;
        byte[] tmp = new byte[magicLen];
        in.readBytes(tmp);
        // 校验魔数
        for (int i = 0; i < magicLen; i++) {
            if (tmp[i] != RpcConstant.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("错误的魔数");
            }
        }
        // 读取版本
        byte version = in.readByte();
        if (version != RpcConstant.VERSION) {
            throw new IllegalArgumentException("错误的版本");
        }
        // 读取消息长度
        int fullLength = in.readInt();
        // 读取其他属性
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        // build RpcMessage
        RpcMessage rpcMessage = RpcMessage.builder()
                .codec(codecType)
                .compressType(compressType)
                .messageType(messageType)
                .requestId(requestId).build();
        if (messageType == RpcConstant.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstant.PING);
        } else if (messageType == RpcConstant.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstant.PONG);
        } else {
            int bodyLength = fullLength - RpcConstant.HEADER_LENGTH;
            if (bodyLength > 0) {
                byte[] bs = new byte[bodyLength];
                in.readBytes(bs);
                // 解压缩
                String compressName = CompressTypeEnum.getName(compressType);
                Compress compressor = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
                bs = compressor.decompress(bs);
                // 反序列化
                String codecName = SerializationTypeEnum.getName(codecType);
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
                if (messageType == RpcConstant.REQUEST_TYPE) {
                    RpcRequest tmpValue = serializer.deserialize(bs, RpcRequest.class);
                    rpcMessage.setData(tmpValue);
                } else if (messageType == RpcConstant.RESPONSE_TYPE) {
                    RpcResponse tmpValue = serializer.deserialize(bs, RpcResponse.class);
                    rpcMessage.setData(tmpValue);
                }
            }
        }
        return rpcMessage;
    }

}
