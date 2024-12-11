package com.xk.rpcclient.proxy;

import com.xk.rpcclient.config.RpcClientProperties;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpcclient.transmission.common.RequestMetadata;
import com.xk.rpccore.discover.ServiceDiscover;
import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.RpcResponse;
import com.xk.rpccore.netcommon.ServiceInfo;
import com.xk.rpccore.protocol.MessageHeader;
import com.xk.rpccore.protocol.RpcMessage;
import com.xk.rpccore.retry.RetryStrategy;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author xk
 * @date 2024/8/19--23:45
 */
@Slf4j
public class RemoteMethodCall {
    public static Object remoteCall(ServiceDiscover discovery, TransClient rpcClient, String serviceName,
                                    RpcClientProperties properties, RetryStrategy retryStrategy, Method method, Object[] args) {
        int attempt = 0;
        while (true) {
            try {
                // 构建请求头
                MessageHeader header = MessageHeader.build(properties.getSerialization());
                // 构建请求体
                RpcRequest request = new RpcRequest();
                request.setServiceName(serviceName);
                request.setMethod(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameterValues(args);

                // 进行服务发现
                ServiceInfo serviceInfo = discovery.discover(request);
                if (serviceInfo == null) {
                    throw new RpcException(String.format("The service [%s] was not found in the remote registry center.",
                            serviceName));
                }
                log.info(request.toString());
                log.info(serviceInfo.toString());
                // 构建通信协议信息
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setHeader(header);
                rpcMessage.setBody(request);

                // 构建请求元数据
                RequestMetadata metadata = RequestMetadata.builder()
                        .rpcMessage(rpcMessage)
                        .serverAddr(serviceInfo.getAddress())
                        .port(serviceInfo.getPort())
                        .timeout(properties.getTimeout()).build();

                // 发送网络请求，获取结果
                RpcMessage responseRpcMessage = rpcClient.sendRpcRequest(metadata);

                if (responseRpcMessage == null) {
                    throw new RpcException("Remote procedure call timeout.");
                }

                // 获取响应结果
                RpcResponse response = (RpcResponse) responseRpcMessage.getBody();

                // 如果远程调用发生错误
                if (response.getExceptionValue() != null) {
                    throw new RpcException(response.getExceptionValue());
                }

                // 返回响应结果
                return response.getReturnValue();
            } catch (RpcException e) {
                attempt++;
                if (retryStrategy.shouldRetry(attempt, e)) {
                    long retryInterval = retryStrategy.getRetryInterval(attempt);
                    log.warn("RPC call failed, retrying in {}ms (attempt {})", retryInterval, attempt);
                    try {
                        Thread.sleep(retryInterval);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw new RpcException("Retry interrupted", ex);
                    }
                } else {
                    throw e; // 超过最大重试次数后抛出异常
                }
            }
        }
    
//        // 构建请求头
//        MessageHeader header = MessageHeader.build(properties.getSerialization());
//        // 构建请求体
//        RpcRequest request = new RpcRequest();
//        
//        request.setServiceName(serviceName);
//        request.setMethod(method.getName());
//        request.setParameterTypes(method.getParameterTypes());
//        request.setParameterValues(args);
//
//        // 进行服务发现
//        ServiceInfo serviceInfo = discovery.discover(request);
//        if (serviceInfo == null) {
//            throw new RpcException(String.format("The service [%s] was not found in the remote registry center.",
//                    serviceName));
//        }
//        log.info(request.toString());
//        log.info(serviceInfo.toString());
//        // 构建通信协议信息
//        RpcMessage rpcMessage = new RpcMessage();
//        rpcMessage.setHeader(header);
//        rpcMessage.setBody(request);
//
//        // 构建请求元数据
//        RequestMetadata metadata = RequestMetadata.builder()
//                .rpcMessage(rpcMessage)
//                .serverAddr(serviceInfo.getAddress())
//                .port(serviceInfo.getPort())
//                .timeout(properties.getTimeout()).build();
//
//        // todo：此处可以实现失败重试机制
//        
//        // 发送网络请求，获取结果
//        RpcMessage responseRpcMessage = rpcClient.sendRpcRequest(metadata);
//        
//        if (responseRpcMessage == null) {
//            throw new RpcException("Remote procedure call timeout.");
//        }
//
//        // 获取响应结果
//        RpcResponse response = (RpcResponse) responseRpcMessage.getBody();
//
//        // 如果 远程调用 发生错误
//        if (response.getExceptionValue() != null) {
//            throw new RpcException(response.getExceptionValue());
//        }
//        // 返回响应结果
//        return response.getReturnValue();
    }
}
