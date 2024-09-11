package com.xk.rpcserver.transmission.netty;

import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.constant.MessageStatus;
import com.xk.rpccore.constant.MessageType;
import com.xk.rpccore.constant.TransConstants;
import com.xk.rpccore.factory.SingletonFactory;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.RpcResponse;
import com.xk.rpccore.protocol.MessageHeader;
import com.xk.rpccore.protocol.RpcMessage;
import com.xk.rpcserver.transmission.common.RpcRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xk
 * @date 2024/8/27--19:36
 */
@Slf4j
public class NettyRequestHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    private final RpcRequestHandler rpcRequestHandler;

    public NettyRequestHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        threadPool.execute(() -> {
            try {
                RpcMessage responseRpcMessage = new RpcMessage();
                MessageHeader header = rpcMessage.getHeader();
                MessageType messageType = MessageType.parseByType(header.getMessageType());
                if (messageType == MessageType.HEARTBEAT_REQUEST) {
                    header.setMessageType(MessageType.HEARTBEAT_RESPONSE.getType());
                    header.setMessageStatus(MessageStatus.SUCCESS.getCode());
                    // 设置响应头部信息
                    responseRpcMessage.setHeader(header);
                    responseRpcMessage.setBody(TransConstants.PONG);
                } else { // 处理 Rpc 请求信息
                    RpcRequest request = (RpcRequest) rpcMessage.getBody();
                    RpcResponse response = new RpcResponse();
                    // 设置头部消息类型
                    header.setMessageType(MessageType.RESPONSE.getType());
                    // 反射调用
                    try {
                        // 获取本地反射调用结果
                        Object result = rpcRequestHandler.handleRpcRequest(request);
                        response.setReturnValue(result);
                        header.setMessageStatus(MessageStatus.SUCCESS.getCode());
                    } catch (Exception e) {
                        log.error("The service [{}], the method [{}] invoke failed!", request.getServiceName(), request.getMethod());
                        // 若不设置，堆栈信息过多，导致报错
                        response.setExceptionValue(new RpcException("Error in remote procedure call, " + e.getMessage()));
                        header.setMessageStatus(MessageStatus.FAIL.getCode());
                    }
                    // 设置响应头部信息
                    responseRpcMessage.setHeader(header);
                    responseRpcMessage.setBody(response);
                }
                log.debug("responseRpcMessage: {}.", responseRpcMessage);
                // 将结果写入，传递到下一个处理器
                channelHandlerContext.writeAndFlush(responseRpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } finally {
                ReferenceCountUtil.release(rpcMessage);
            }

        });
    }
}
