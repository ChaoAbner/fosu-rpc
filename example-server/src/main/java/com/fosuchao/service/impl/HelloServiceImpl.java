package com.fosuchao.service.impl;

import com.fosuchao.Hello;
import com.fosuchao.HelloService;
import com.fosuchao.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Chao Ye on 2021/2/19
 */
@Slf4j
@RpcService(version = "version1", group = "test1")
public class HelloServiceImpl implements HelloService {

    static {
        System.out.println("创建HelloServiceImpl");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到消息：{}", hello.getMessage());
        String result = "HelloServiceImpl响应消息";
        log.info("HelloServiceImpl返回：{}", result);
        return result;
    }
}
