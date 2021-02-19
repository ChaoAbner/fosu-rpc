package com.fosuchao;

import com.fosuchao.annotation.RpcReference;
import org.springframework.stereotype.Component;

/**
 * Created by Chao Ye on 2021/2/19
 */
@Component
public class HelloController {

    @RpcReference(version = "version1", group = "test1")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("hello~~~", "描述"));
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("message-" + i, "描述-" + i)));
        }
    }
}
