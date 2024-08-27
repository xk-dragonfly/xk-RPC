package com.xk.rpcclient.transmission.netty;

import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpccore.RpcException;
import com.xk.rpccore.netcommon.RequestMetadata;
import com.xk.rpccore.protocol.RpcMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author xk
 * @date 2024/8/20--19:49
 */
@Slf4j
public class NettyTransClient implements TransClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    private final ChannelCache channelCache;

    
    public NettyTransClient(){
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IdleStateHandler(0, 15, 0));
                        
                    }
                });
        this.channelCache = new ChannelCache();
    }

    @Override
    public RpcMessage sendRpcRequest(RequestMetadata requestMetadata) {
        

        return null;
    }


    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelCache.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelCache.set(inetSocketAddress, channel);
        }
        return channel;
    }

    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        // 方式一，不用打印提示信息可以使用的方案
        // sync() 同步等待异步connect连接成功
//        Channel channel = bootstrap.connect(inetSocketAddress).sync().channel();
        // 设置同步等待异步关闭完成
//        channel.closeFuture().sync();

        // 方式二，打印提示信息使用方案
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.debug("The client has successfully connected to server [{}]!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new RpcException(String.format("The client failed to connect to [%s].", inetSocketAddress.toString()));
            }
        });
        // 等待 future 完成返回结果
        Channel channel = completableFuture.get();
        // 添加异步关闭之后的操作
        channel.closeFuture().addListener(future -> {
            log.info("The client has been disconnected from server [{}].", inetSocketAddress.toString());
        });
        return channel;
    }

}
