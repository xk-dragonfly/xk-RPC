package com.xk.rpccore.netcommon;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xk
 * @date 2024/8/19--23:36
 */
@Data
public class RpcRequest implements Serializable {
    /**
     * 服务名称：请求的服务名 + 版本
     */
    private String serviceName;

    /**
     * 请求调用的方法名称
     */
    private String method;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数
     */
    private Object[] parameterValues;
}
