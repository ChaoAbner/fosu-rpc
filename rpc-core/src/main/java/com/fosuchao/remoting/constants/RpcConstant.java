package com.fosuchao.remoting.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Chao Ye on 2021/1/14
 * desc：Rpc常量
 */
public class RpcConstant {

    /**
     * 魔数
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'f', (byte) 'o', (byte) 's', (byte) 'u'};

    /**
     * 默认编码
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 版本信息
     */
    public static final byte VERSION = 1;

    /**
     * 总长度
     */
    public static final byte TOTAL_LENGTH = 16;

    /**
     * 消息类型
     */
    public static final byte REQUEST_TYPE = 1;

    public static final byte RESPONSE_TYPE = 2;

    public static final byte HEARTBEAT_REQUEST_TYPE = 3;

    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;

    /**
     * 头部长度
     */
    public static final byte HEADER_LENGTH = 16;

    /**
     * ping/pong
     */
    public static final String PING = "ping";

    public static final String PONG = "pong";

    /**
     * 包大小最大限制8MB
     */
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}
