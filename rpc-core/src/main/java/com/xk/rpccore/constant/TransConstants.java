package com.xk.rpccore.constant;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xk
 * @date 2024/8/27--19:46
 */
public class TransConstants {
    private static final AtomicInteger ai = new AtomicInteger();

    /**
     * 魔数，用来第一时间判断是否无效数据包
     */
    public static final byte[] MAGIC_NUM = new byte[]{(byte) 'x', (byte) 'r', (byte) 'p', (byte) 'c'};

    public static final byte VERSION = 1;

    public static final String PING = "ping";

    public static final String PONG = "pong";

    public static int getSequenceId() {
        // todo: 实现原子操作
        return ai.getAndIncrement();
    }
}
