package com.fosuchao.remoting.transport.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chao Ye on 2021/1/18
 */
@Slf4j
public class ChannelProvider {

    private static final Map<String, Channel> CHANNELS_MAP = new ConcurrentHashMap<>();

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (CHANNELS_MAP.containsKey(key)) {
            Channel channel = CHANNELS_MAP.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                CHANNELS_MAP.remove(key);
            }
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        CHANNELS_MAP.put(key, channel);
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        CHANNELS_MAP.remove(key);
        log.info("Channel map 删除channel：[{}]，当前map大小：[{}]", key, CHANNELS_MAP.size());
    }
}
