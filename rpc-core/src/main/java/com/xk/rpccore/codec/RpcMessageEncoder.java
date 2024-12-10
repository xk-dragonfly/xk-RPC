package com.xk.rpccore.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author xk
 * @version 1.0
 * @ClassName RpcMessageEncoder
 * @Date 2024/12/4 23:15
 */
public class RpcMessageEncoder<T> extends MessageToByteEncoder<T> {

    @Override
    protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
        // todo: implement this method.
    }
}
