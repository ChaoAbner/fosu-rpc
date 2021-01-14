package com.fosuchao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Chao Ye on 2021/1/14
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcServiceProperties {

    /**
     * service的版本
     */
    private String version;

    /**
     * 当接口有多个实现类的时候，用group区分
     */
    private String group;

    private String serviceName;

    /**
     * 返回服务注册的名字
     */
    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }
}
