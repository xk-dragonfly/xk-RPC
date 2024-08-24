package com.xk.rpcclient.transmission.common;

import com.xk.rpccore.protocol.RpcMessage;

/**
 * @author xk
 * @date 2024/8/24--14:40
 */
public class RpcRequestMetadata {
    //rpc自定义的消息协议，包含请求头和请求消息
    private RpcMessage rpcMessage;
    
    //服务提供者ip
    private String serverAddress;
    
    //服务提供者端口
    private Integer port;
    
    //请求超时时间范围
    private Integer timeout;
    
}
