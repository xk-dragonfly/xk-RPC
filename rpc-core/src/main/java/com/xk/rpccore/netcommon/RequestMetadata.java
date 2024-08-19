package com.xk.rpccore.netcommon;

import com.xk.rpccore.protocol.RpcMessage;

/**
 * @author xk
 * @date 2024/8/19--23:58
 */
public class RequestMetadata {
    /**
     * 消息协议 - （请求头协议信息 + 请求信息）
     */
    private RpcMessage rpcMessage;

    /**
     * 远程服务提供方地址
     */
    private String serverAddr;

    /**
     * 远程服务提供方端口号
     */
    private Integer port;

    /**
     * 调用超时时间
     */
    private Integer timeout;
}
