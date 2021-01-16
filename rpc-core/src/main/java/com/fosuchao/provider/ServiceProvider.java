package com.fosuchao.provider;

import com.fosuchao.entity.RpcServiceProperties;

/**
 * Created by Chao Ye on 2021/1/16
 */
public interface ServiceProvider {

    /**
     * 添加service
     * @param service               service对象
     * @param serviceClass          继承自service接口的Class对象
     * @param rpcServiceProperties  service相关的属性
     */
    void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties);

    /**
     * 获取service
     * @param rpcServiceProperties  service相关的属性
     * @return                      service对象
     */
    Object getService(RpcServiceProperties rpcServiceProperties);

    /**
     * 发布service
     * @param service               service对象
     * @param rpcServiceProperties  service相关属性
     */
    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * 发布service
     * @param service               service对象
     */
    void publishService(Object service);
}
