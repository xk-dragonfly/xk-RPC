package com.xk.rpccore.register;

import com.xk.rpccore.netcommon.ServiceInfo;

/**
 * @author xk
 * @date 2024/8/20--20:28
 */
public interface ServiceRegister {
    
    //注册/重新注册一个服务信息到 注册中心
    void register(ServiceInfo serviceInfo) throws Exception;

    //移除一个服务信息从 注册中心
    void unregister(ServiceInfo serviceInfo) throws Exception;

    //关闭与服务注册中心的连接
    void destroy() throws Exception;
}
