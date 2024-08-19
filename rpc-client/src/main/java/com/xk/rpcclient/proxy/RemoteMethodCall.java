package com.xk.rpcclient.proxy;

import com.xk.rpcclient.config.RpcClientProperties;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.discover.ServiceDiscover;
import com.xk.rpccore.netcommon.RpcResponse;

import java.lang.reflect.Method;

/**
 * @author xk
 * @date 2024/8/19--23:45
 */
public class RemoteMethodCall {
    public static Object remoteCall(ServiceDiscover discovery, TransClient rpcClient, String serviceName,
                                    RpcClientProperties properties, Method method, Object[] args) {
        return new RpcResponse().getReturnValue();
    }
}
