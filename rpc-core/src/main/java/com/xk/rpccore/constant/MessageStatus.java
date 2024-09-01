package com.xk.rpccore.constant;

import lombok.Getter;

/**
 * @author xk
 * @date 2024/8/27--19:45
 */
public enum MessageStatus {
    /**
     * 成功
     */
    SUCCESS((byte) 0),

    /**
     * 失败
     */
    FAIL((byte) 1);

    @Getter
    private final byte code;

    MessageStatus(byte code) {
        this.code = code;
    }

    public static boolean isSuccess(byte code) {
        return MessageStatus.SUCCESS.code == code;
    }
}
