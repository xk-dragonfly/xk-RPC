package com.xk.rpcserver.transmission.netty;

import com.xk.rpccore.codec.RpcFrameDecoder;
import com.xk.rpccore.codec.RpcMessageCodec;
import com.xk.rpcserver.transmission.TransServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author xk
 * @date 2024/8/27--19:29
 */
@Slf4j
public class NettyServer implements TransServer {

    @SneakyThrows
    @Override
    public void start(Integer port) {
        // boss 处理 accept 事件
        EventLoopGroup boss = new NioEventLoopGroup();
        // worker 处理 read/write 事件
        EventLoopGroup worker = new NioEventLoopGroup();

        InetAddress localHost = InetAddress.getLocalHost();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.SO_BACKLOG,128)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new RpcFrameDecoder());
                        socketChannel.pipeline().addLast(new RpcMessageCodec());
                        socketChannel.pipeline().addLast(new NettyRequestHandler());
                    }
                });
    }
}
