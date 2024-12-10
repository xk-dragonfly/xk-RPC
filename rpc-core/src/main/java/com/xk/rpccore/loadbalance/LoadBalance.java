package com.xk.rpccore.loadbalance;

import com.xk.rpccore.extension.SPI;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.ServiceInfo;

import java.util.List;

/**
 * @author xk  LoadBalance
 * @date 2024/8/27--20:42
 */
@SPI
public interface LoadBalance {
    ServiceInfo select(List<ServiceInfo> invokers, RpcRequest request);
}
