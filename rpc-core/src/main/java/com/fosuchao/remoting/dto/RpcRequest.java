package com.fosuchao.remoting.dto;

import com.fosuchao.entity.RpcServiceProperties;
import lombok.*;

import java.io.Serializable;

/**
 * Created by Chao Ye on 2021/1/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 567705521652488183L;

    /**
     * request ID
     */
    private String requestId;

    /**
     * 调用的接口名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private Object[] parameters;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 组名
     */
    private String group;

    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder()
                .serviceName(this.getInterfaceName())
                .group(this.getGroup())
                .version(this.getVersion()).build();
    }

}
