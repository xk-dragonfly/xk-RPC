package com.xk.rpccore.retry.impl;

import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.retry.RetryStrategy;

/**
 * @author xk
 * @date 2024/12/11--22:33
 */
public class FixedIntervalRetryStrategy implements RetryStrategy {
    private final long retryInterval; // 固定的重试间隔（毫秒）
    private final int maxRetries; // 最大重试次数

    public FixedIntervalRetryStrategy(long retryInterval, int maxRetries) {
        this.retryInterval = retryInterval;
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean shouldRetry(int attempt, RpcException exception) {
        return attempt < maxRetries;
    }

    @Override
    public long getRetryInterval(int attempt) {
        return retryInterval; // 每次重试间隔相同
    }
}

