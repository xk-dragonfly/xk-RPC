package com.xk.rpccore.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author xk
 * @date 2024/8/27--19:49
 */
public class RpcFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 得到当前约定协议的帧解码器，
     * <pre>{@code
     *    this.RpcFrameDecoder(1024, 12, 4)
     * }</pre>
     * 引用：{@link RpcFrameDecoder#RpcFrameDecoder(int, int, int)}
     */
    public RpcFrameDecoder() {
        this(1024, 12, 4);
    }

    /**
     * 构造方法
     *
     * @param maxFrameLength    数据帧的最大长度
     * @param lengthFieldOffset 长度域的偏移字节数
     * @param lengthFieldLength 长度域所占的字节数
     */
    public RpcFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
}
