package com.fosuchao.exception;

import com.fosuchao.enums.RpcErrorMessageEnum;

/**
 * Created by Chao Ye on 2021/1/15
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

}
