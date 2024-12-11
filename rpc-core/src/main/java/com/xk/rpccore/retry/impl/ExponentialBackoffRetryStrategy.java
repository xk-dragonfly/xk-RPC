package com.xk.rpccore.retry.impl;

import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.retry.RetryStrategy;

/**
 * @author xk
 * @date 2024/12/11--22:33
 */
public class ExponentialBackoffRetryStrategy implements RetryStrategy {
    private final long initialInterval; // 初始间隔
    private final int maxRetries; // 最大重试次数

    public ExponentialBackoffRetryStrategy(long initialInterval, int maxRetries) {
        this.initialInterval = initialInterval;
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean shouldRetry(int attempt, RpcException exception) {
        return attempt < maxRetries;
    }

    @Override
    public long getRetryInterval(int attempt) {
        // 指数退避算法
        return initialInterval * (1L << attempt); // 每次重试间隔加倍
    }
}

