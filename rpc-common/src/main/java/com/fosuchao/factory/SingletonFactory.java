package com.fosuchao.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chao Ye on 2021/1/16
 */
public class SingletonFactory {

    private static final Map<Class<?>, Object> OBJECT_MAP = new HashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> clazz) {
        Object instance = OBJECT_MAP.get(clazz);
        if (instance == null) {
            synchronized (SingletonFactory.class) {
                instance = OBJECT_MAP.get(clazz);
                if (instance == null) {
                    try {
                        instance = clazz.getDeclaredConstructor().newInstance();
                        OBJECT_MAP.put(clazz, instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return clazz.cast(instance);
    }
}
