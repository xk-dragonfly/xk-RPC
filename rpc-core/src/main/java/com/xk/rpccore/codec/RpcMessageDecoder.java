package com.xk.rpccore.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xk
 * @version 1.0
 * @ClassName RpcMessageDecoder
 * @Date 2024/12/4 12:41
 */
public class RpcMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // todo: implement this method
    }
}
