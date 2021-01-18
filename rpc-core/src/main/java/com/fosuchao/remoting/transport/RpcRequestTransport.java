package com.fosuchao.remoting.transport;

import com.fosuchao.remoting.dto.RpcRequest;

/**
 * Created by Chao Ye on 2021/1/18
 */
public interface RpcRequestTransport {

    /**
     * 发送rpc请求，获取响应结果
     * @param rpcRequest        消息体
     * @return                  sever响应数据
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
