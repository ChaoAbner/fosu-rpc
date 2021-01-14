package com.fosuchao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Chao Ye on 2021/1/14
 */
@Getter
@AllArgsConstructor
@ToString
public enum RpcResponseCodeEnum {

    SUCCESS(200, "远程调用成功"),
    FAIL(500, "远程调用失败");

    private final int code;

    private final String message;
}
