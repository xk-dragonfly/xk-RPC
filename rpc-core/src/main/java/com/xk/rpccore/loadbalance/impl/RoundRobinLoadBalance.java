package com.xk.rpccore.loadbalance.impl;


import com.xk.rpccore.loadbalance.AbstractLoadBalance;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.ServiceInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡算法
 *
 * @author xk
 * @version 1.0
 * @ClassName RoundRobinLoadBalance
 * @Date 2024/12/1 12:14
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> invokers, RpcRequest request) {
        return invokers.get(getAndIncrement() % invokers.size());
    }

    /**
     * 返回当前值并加一，通过 CAS 原子更新，当当前值到达 {@link Integer#MAX_VALUE} 时，重新设值为 0
     *
     * @return 返回当前的值
     */
    public final int getAndIncrement() {
        int prev, next;
        do {
            prev = atomicInteger.get();
            next = prev == Integer.MAX_VALUE ? 0 : prev + 1;
        } while (!atomicInteger.compareAndSet(prev, next));
        return prev;
    }

}
