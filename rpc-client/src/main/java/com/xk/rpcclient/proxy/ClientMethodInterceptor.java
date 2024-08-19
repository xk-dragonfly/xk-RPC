package com.xk.rpcclient.proxy;

import com.xk.rpcclient.config.RpcClientProperties;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.discover.ServiceDiscover;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.Callback;

import java.lang.reflect.Method;

/**
 * @author xk
 * @date 2024/8/19--23:46
 */
public class ClientMethodInterceptor implements MethodInterceptor, Callback {
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

    public ClientMethodInterceptor(ServiceDiscover serviceDiscover, TransClient transClient, RpcClientProperties properties, String serviceName) {
        this.serviceDiscover = serviceDiscover;
        this.transClient = transClient;
        this.properties = properties;
        this.serviceName = serviceName;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 执行远程方法调用
        return RemoteMethodCall.remoteCall(serviceDiscover, transClient, serviceName, properties, method, args);
    }
}
