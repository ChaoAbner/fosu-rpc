package com.fosuchao.utils;

/**
 * Created by Chao Ye on 2021/1/16
 */
public class RuntimeUtil {

    public static int getCpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
