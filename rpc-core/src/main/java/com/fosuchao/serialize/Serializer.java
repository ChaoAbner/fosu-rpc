package com.fosuchao.serialize;

import com.fosuchao.extension.SPI;

/**
 * Created by Chao Ye on 2021/1/18
 */
@SPI
public interface Serializer {

    /**
     * 序列化
     * @param obj      对象
     * @return         序列化后的字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param bytes    序列化后的字节数组
     * @param clazz    目标类
     * @param <T>      类的类型
     * @return         反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
