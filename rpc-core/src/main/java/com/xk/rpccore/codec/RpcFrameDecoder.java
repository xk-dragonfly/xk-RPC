package com.xk.rpccore.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author xk
 * @date 2024/8/27--19:49
 */
public class RpcFrameDecoder extends LengthFieldBasedFrameDecoder {

    public RpcFrameDecoder() {
        this(1024, 12, 4);
    }
    
    public RpcFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
}
