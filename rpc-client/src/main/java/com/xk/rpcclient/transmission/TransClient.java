package com.xk.rpcclient.transmission;

import com.xk.rpccore.netcommon.RequestMetadata;
import com.xk.rpccore.protocol.RpcMessage;

/**
 * @author xk
 * @date 2024/8/19--23:37
 */
public interface TransClient {
    /**
     * 发起远程过程调用
     *
     * @param requestMetadata rpc 请求元数据
     * @return 响应结果
     */
    RpcMessage sendRpcRequest(RequestMetadata requestMetadata);
}
