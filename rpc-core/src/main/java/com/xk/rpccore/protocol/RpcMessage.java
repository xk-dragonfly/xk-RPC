package com.xk.rpccore.protocol;

import lombok.Data;

/**
 * @author xk
 * @date 2024/8/19--23:53
 */
@Data
public class RpcMessage {
    /**
     * 请求头 - 协议信息
     */
    private MessageHeader header;

    /**
     * 消息体
     */
    private Object body;
}
