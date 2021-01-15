package com.fosuchao.extension;

/**
 * Created by Chao Ye on 2021/1/15
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
