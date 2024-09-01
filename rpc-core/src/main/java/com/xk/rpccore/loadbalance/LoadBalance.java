package com.xk.rpccore.loadbalance;

import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.ServiceInfo;

import java.util.List;

/**
 * @author xk
 * @date 2024/8/27--20:42
 */

public interface LoadBalance {
    ServiceInfo select(List<ServiceInfo> invokers, RpcRequest request);
}
