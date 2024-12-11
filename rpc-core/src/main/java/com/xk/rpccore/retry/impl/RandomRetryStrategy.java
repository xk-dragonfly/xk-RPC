package com.xk.rpccore.retry.impl;

import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.retry.RetryStrategy;

/**
 * @author xk
 * @date 2024/12/11--22:36
 */
public class RandomRetryStrategy implements RetryStrategy {
    private final long minInterval; // 最小间隔时间（毫秒）
    private final long maxInterval; // 最大间隔时间（毫秒）
    private final int maxRetries; // 最大重试次数

    public RandomRetryStrategy(long minInterval, long maxInterval, int maxRetries) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.maxRetries = maxRetries;
    }

    @Override
    public boolean shouldRetry(int attempt, RpcException exception) {
        return attempt < maxRetries;
    }

    @Override
    public long getRetryInterval(int attempt) {
        // 随机间隔时间
        return minInterval + (long) (Math.random() * (maxInterval - minInterval));
    }
}


