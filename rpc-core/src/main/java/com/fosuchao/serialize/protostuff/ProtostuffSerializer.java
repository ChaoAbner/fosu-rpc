package com.fosuchao.serialize.protostuff;

import com.fosuchao.serialize.Serializer;

/**
 * Created by Chao Ye on 2021/1/18
 */
public class ProtostuffSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
