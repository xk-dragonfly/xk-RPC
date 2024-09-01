package com.xk.rpccore.loadbalance.impl;

import com.xk.rpccore.loadbalance.AbstractLoadBalance;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.ServiceInfo;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡随机策略
 * @author xk
 * @date 2024/8/28--13:33
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    
    final Random random = new Random();
    
    @Override
    protected ServiceInfo doSelect(List<ServiceInfo> invokers, RpcRequest request) {
        
        return invokers.get(random.nextInt(invokers.size()));
    }
}
