package com.fosuchao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Chao Ye on 2021/1/15
 */
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),

    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
