package com.xk.rpcclient.proxy;

import com.xk.rpcclient.config.RpcClientProperties;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.discover.ServiceDiscover;
import com.xk.rpccore.retry.RetryStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK动态代理，处理通过代理对象调用的方法，将这些方法的调用转发给远程服务进行实际的 RPC 调用
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

    /**
     * 重试策略
     */
    private final RetryStrategy retryStrategy;


    public ClientInvocationHandler(ServiceDiscover serviceDiscover, TransClient transClient, RpcClientProperties properties, String serviceName, RetryStrategy retryStrategy) {
        this.serviceDiscover = serviceDiscover;
        this.transClient = transClient;
        this.properties = properties;
        this.serviceName = serviceName;
        this.retryStrategy = retryStrategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 执行远程方法调用
        return RemoteMethodCall.remoteCall(serviceDiscover, transClient, serviceName, properties, retryStrategy, method, args);
    }
}
