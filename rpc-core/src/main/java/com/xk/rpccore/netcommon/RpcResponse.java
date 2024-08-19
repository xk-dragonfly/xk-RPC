package com.xk.rpccore.netcommon;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xk
 * @date 2024/8/19--23:38
 */
@Data
public class RpcResponse implements Serializable {
    /**
     * 请求返回值
     */
    private Object returnValue;

    /**
     * 发生异常时的异常信息
     */
    private Exception exceptionValue;
}
