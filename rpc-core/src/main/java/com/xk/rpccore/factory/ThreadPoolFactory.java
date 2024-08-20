package com.xk.rpccore.factory;

import com.xk.rpccore.config.ThreadPoolConfig;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xk
 * @date 2024/8/20--20:27
 */
public class ThreadPoolFactory {
    private static final int AVAILABLE_PROCESSOR_NUMBER = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolConfig threadPoolConfig;

    public ThreadPoolFactory() {
        threadPoolConfig = new ThreadPoolConfig();
    }

    public static ThreadPoolExecutor getDefaultThreadPool() {
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                threadPoolConfig.getMaximumPoolSize(),
                threadPoolConfig.getKeepAliveTime(),
                threadPoolConfig.getTimeUnit(),
                threadPoolConfig.getBlockingQueue());
    }
}
