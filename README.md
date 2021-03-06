## 介绍

fosu-rpc是基于Netty+Zookeeper+Protostuff实现的RPC框架。

项目开发流程和详情请查看：[详情](https://github.com/ChaoAbner/fosu-rpc/tree/master/docs)

## 功能

目前框架实现的主要功能如下

1. 注册中心（zk）
2. 网络传输（远程调用）
3. 序列化和反序列化
4. 提供多种负载均衡策略
5. 心跳保持
6. SPI机制的运用

## 简单设计思路

一个最简单的RPC框架的架构图如下，也是fosu-rpc的架构：

![image-20210113151657579](http://img.fosuchao.com/image-20210113151657579.png)

服务提供者向注册中心（zk）注册服务，服务消费者从注册中心获取服务的相关信息，比如调用的地址，再通过网络请求向服务提供者发起调用。

再来看看大名鼎鼎的Dubbo的架构图

![image-20210113152005122](http://img.fosuchao.com/image-20210113152005122.png)

一般情况下，一个RPC框架不仅仅需要提供服务发现，远程调用的功能，还要提供负载均衡、容错等功能，这些都是封装到框架内部，对调用者无感知的，这是跟普通HTTP请求不太一样的地方。

## 项目构成

项目的模块概览如下图

![image-20210113152515468](http://img.fosuchao.com/image-20210113152515468.png)

## 运行

>  todo

## 谈谈为什么要用RPC

当我们的系统拆分成很多个子系统的时候，子系统之间可能要相互调用对方的服务，RPC就是为了解决这种远程调用的问题而出现的，那么既然是解决远程调用，为什么不选择直接使用HTTP请求而要使用RPC呢？

首先RPC框架跟HTTP不是对立面，RPC中可以使用HTTP作为通讯协议。**RPC是一种设计、实现框架，通讯协议只是其中一部分。**

### 传输协议和序列化协议

RPC是远端过程调用，其调用协议通常包含传输协议和序列化协议。

传输协议包含: 如著名的gRPC使用的http2协议，也有如dubbo一类的自定义报文的tcp协议。

序列化协议包含: 如基于文本编码的xml，json也有二进制编码的protobuf，hessian等。

> 需要注意的是HTTP相对于RPC的缺点并不是连接的建立与断开过于频繁所导致的开销，HTTP也是有复用TCP连接的，并不会频繁的连接和断开。其次是HTTP也能使用像protobuf这种二进制编码的序列化协议对内容进行编码，所以二者最大的区别其实是传输协议

### 分析原因

不是说RPC不能用HTTP作为传输协议，比如gRPC就是使用http2作为传输协议。

但是基于HTTP1.1的TCP报文包含太多废信息，比如说编码信息body是使用的二进制编码协议，但报文元数据header头的键值却使用了文本编码，很占用字节数，同时传输效率就慢了。

再看自定义的TCP协议报文：

![image-20210113151024668](http://img.fosuchao.com/image-20210113151024668.png)

报头占用的字节数也就只有16个byte，极大地精简了传输内容。

这就是通常采用自定义TCP协议的RPC来通信的原因。
