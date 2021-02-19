package com.fosuchao.service.impl;

import com.fosuchao.Hello;
import com.fosuchao.HelloService;
import com.fosuchao.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chao Ye on 2021/2/19
 */
@Slf4j
@RpcService(version = "version2", group = "test2")
public class HelloServiceImpl2 implements HelloService {

    static {
        System.out.println("创建HelloServiceImpl2");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl2收到消息：{}", hello.getMessage());
        String result = "HelloServiceImpl2响应消息";
        log.info("HelloServiceImpl2返回：{}", result);
        return result;
    }
}
