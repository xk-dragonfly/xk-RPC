package com.xk.rpcserver.transmission.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SslDetectionHandler extends ByteToMessageDecoder {
    private static final int SSL_RECORD_HEADER = 0x16;
    private final SslContext sslContext;

    public SslDetectionHandler(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 1) {
            return;
        }

        if (in.getUnsignedByte(0) == SSL_RECORD_HEADER) {
            // 检测到SSL握手，添加SSL处理器
            ctx.pipeline().addAfter(ctx.name(), "ssl", sslContext.newHandler(ctx.alloc()));
            log.info("SSL connection detected, SSL handler added to pipeline");
        }else{
            log.info("Non-SSL connection detected");
        }
        
        // 移除本处理器
        ctx.pipeline().remove(this);
    }
} 