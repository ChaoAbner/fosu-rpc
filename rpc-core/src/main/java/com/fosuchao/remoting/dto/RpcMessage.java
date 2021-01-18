package com.fosuchao.remoting.dto;

import lombok.*;

/**
 * Created by Chao Ye on 2021/1/14
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcMessage {

    /**
     * rpc message类型
     */
    private byte messageType;

    /**
     * 序列化类型
     */
    private byte codec;

    /**
     * 压缩类型
     */
    private byte compressType;

    /**
     * request ID
     */
    private int requestId;

    /**
     * 数据
     */
    private Object data;

}
