package com.xk.rpcserver.transmission.netty;

import com.xk.rpccore.codec.RpcFrameDecoder;
import com.xk.rpccore.codec.RpcMessageCodec;
import com.xk.rpcserver.transmission.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @author xk
 * @date 2024/8/27--19:29
 */
@Slf4j
public class NettyServer implements RpcServer {

    @SneakyThrows
    @Override
    public void start(Integer port) {
        // boss 处理 accept 事件
        EventLoopGroup boss = new NioEventLoopGroup();
        // worker 处理 read/write 事件
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            InetAddress inetAddress = InetAddress.getLocalHost();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    // 当客户端第一次请求时才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 准备SSL上下文，但暂不添加到pipeline
                            SslContext sslContext = ServerSslContext.createServerSslContext();
                            
                            // 添加SSL检测处理器
                            ch.pipeline().addLast(new SslDetectionHandler(sslContext));
                            
                            // 添加其他处理器
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new RpcFrameDecoder());
                            ch.pipeline().addLast(new RpcMessageCodec());
                            ch.pipeline().addLast(new NettyRequestHandler());
                        }
                    });
            // 绑定端口，同步等待绑定成功
            ChannelFuture channelFuture = serverBootstrap.bind(inetAddress, port).sync();
            log.debug("Rpc server add {} started on the port {}.", inetAddress, port);
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (UnknownHostException | InterruptedException e) {
            log.error("An error occurred while starting the rpc service.", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
