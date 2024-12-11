package com.xk.rpccore.retry;

import com.xk.rpccore.exception.RpcException;

/**
 * @author xk
 * @date 2024/12/11--22:31
 */
public interface RetryStrategy {
    
    boolean shouldRetry(int attempt, RpcException exception);
    
    long getRetryInterval(int attempt);
}
