package com.fosuchao;

import com.fosuchao.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Chao Ye on 2021/2/19
 */
@RpcScan(basePackage = {"com.fosuchao"})
public class NettyClientMain {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
