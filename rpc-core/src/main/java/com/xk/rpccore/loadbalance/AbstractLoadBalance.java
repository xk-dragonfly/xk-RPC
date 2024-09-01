package com.xk.rpccore.loadbalance;

import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.ServiceInfo;

import java.util.List;

/**
 * @author xk
 * @date 2024/8/28--13:32
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public ServiceInfo select(List<ServiceInfo> invokers, RpcRequest request) {
        if (invokers == null || invokers.isEmpty()) {
            return null;
        }
        // 如果服务列表中只有一个服务，无需进行负载均衡，直接返回
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        // 进行负载均衡，由具体的子类实现
        return doSelect(invokers, request);
    }

    protected abstract ServiceInfo doSelect(List<ServiceInfo> invokers, RpcRequest request);

}
