package com.fosuchao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Chao Ye on 2021/1/18
 */
@Getter
@AllArgsConstructor
public enum CompressTypeEnum {

    GZIP((byte) 0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.getName();
            }
        }
        return null;
    }
}
