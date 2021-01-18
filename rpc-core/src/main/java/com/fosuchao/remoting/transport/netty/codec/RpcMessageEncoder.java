package com.fosuchao.remoting.transport.netty.codec;

import com.fosuchao.compress.Compress;
import com.fosuchao.enums.CompressTypeEnum;
import com.fosuchao.enums.SerializationTypeEnum;
import com.fosuchao.extension.ExtensionLoader;
import com.fosuchao.remoting.constants.RpcConstant;
import com.fosuchao.remoting.dto.RpcMessage;
import com.fosuchao.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

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
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        try {
            // 写魔数
            out.writeBytes(RpcConstant.MAGIC_NUMBER);
            // 版本
            out.writeByte(RpcConstant.VERSION);
            // 跳过消息长度的四个字节
            out.writerIndex(out.writerIndex() + 4);
            // 消息类型
            byte messageType = msg.getMessageType();
            out.writeByte(messageType);
            // 编码
            out.writeByte(msg.getCodec());
            // 压缩类型
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            // 消息长度计算
            byte[] bodyBytes = null;
            int fullLength = RpcConstant.HEADER_LENGTH;
            if (messageType != RpcConstant.HEARTBEAT_REQUEST_TYPE && messageType != RpcConstant.HEARTBEAT_RESPONSE_TYPE) {
                // 如果消息类型不是心跳，那么fullLength = 头长度 + body长度
                String codecName = SerializationTypeEnum.getName(msg.getCodec());
                log.info("codec name: [{}]", codecName);
                // 序列化
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
                bodyBytes = serializer.serialize(msg);
                // 压缩
                String compressName = CompressTypeEnum.getName(msg.getCompressType());
                Compress compressor = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
                bodyBytes = compressor.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writeIndex = out.writerIndex();
            // 写入消息长度
            out.writerIndex(writeIndex - fullLength + RpcConstant.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            // 恢复之前的writeIndex
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("编码失败", e);
        }
    }
}
