package com.xk.rpccore.config;

import lombok.Data;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author xk
 * @date 2024/8/20--20:24
 */
@Data
public class ThreadPoolConfig {
    
    private static final int DEFAULT_CORE_POOL_SIZE = 10;

    private static final int DEFAULT_MAX_POOL_SIZE = 100;

    private static final long DEFAULT_KEEP_ALIVE_TIME = 60L;

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private static final int DEFAULT_BLOCKING_QUEUE_SIZE = 10000;

    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;

    private int maximumPoolSize = DEFAULT_MAX_POOL_SIZE;

    private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    
    private TimeUnit timeUnit = DEFAULT_TIME_UNIT;
    
    private int blockingQueueSize = DEFAULT_BLOCKING_QUEUE_SIZE;
    
    private BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(blockingQueueSize);
}
