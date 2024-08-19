package com.xk.rpcclient.proxy;

import com.xk.rpcclient.config.RpcClientProperties;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.discover.ServiceDiscover;
import com.xk.rpccore.util.ServiceUtil;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xk
 * @date 2024/8/19--23:05
 */
public class ClientProxyFactory {
    /**
     * 服务发现中心实现类
     */
    private final ServiceDiscover discover;


    /**
     * RpcClient 传输实现类
     */
    private final TransClient transClient;


    /**
     * 客户端配置属性
     */
    private final RpcClientProperties properties;

    public ClientProxyFactory(ServiceDiscover discover, TransClient transClient, RpcClientProperties properties) {
        this.discover = discover;
        this.transClient = transClient;
        this.properties = properties;
    }

    /**
     * 代理对象缓存
     */
    private static final Map<String, Object> proxyMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz, String version) {
        return (T) proxyMap.computeIfAbsent(ServiceUtil.serviceKey(clazz.getName(), version), serviceName -> {
            // 如果目标类是一个接口或者 是 java.lang.reflect.Proxy 的子类 则默认使用 JDK 动态代理
            if (clazz.isInterface() || Proxy.isProxyClass(clazz)) {

                return Proxy.newProxyInstance(clazz.getClassLoader(),
                        new Class[]{clazz}, // 注意，这里的接口是 clazz 本身（即，要代理的实现类所实现的接口）
                        new ClientInvocationHandler(discover, transClient, properties, serviceName));
            } else { // 使用 CGLIB 动态代理
                // 创建动态代理增加类
                Enhancer enhancer = new Enhancer();
                // 设置类加载器
                enhancer.setClassLoader(clazz.getClassLoader());
                // 设置被代理类
                enhancer.setSuperclass(clazz);
                // 设置方法拦截器
                enhancer.setCallback(new ClientMethodInterceptor(discover, transClient, properties, serviceName));
                // 创建代理类
                return enhancer.create();
            }
        });
    }
}
