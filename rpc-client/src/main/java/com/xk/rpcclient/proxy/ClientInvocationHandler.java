package com.xk.rpcclient.proxy;

import com.xk.rpcclient.config.RpcClientProperties;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.discover.ServiceDiscover;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author xk
 * @date 2024/8/19--23:43
 */
public class ClientInvocationHandler implements InvocationHandler {
    /**
     * 服务发现中心
     */
    private final ServiceDiscover serviceDiscover;

    /**
     * Rpc客户端
     */
    private final TransClient transClient;

    /**
     * Rpc 客户端配置属性
     */
    private final RpcClientProperties properties;

    /**
     * 服务名称：接口-版本
     */
    private final String serviceName;


    public ClientInvocationHandler(ServiceDiscover serviceDiscover, TransClient transClient, RpcClientProperties properties, String serviceName) {
        this.serviceDiscover = serviceDiscover;
        this.transClient = transClient;
        this.properties = properties;
        this.serviceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 执行远程方法调用
        return RemoteMethodCall.remoteCall(serviceDiscover, transClient, serviceName, properties, method, args);
    }
}
