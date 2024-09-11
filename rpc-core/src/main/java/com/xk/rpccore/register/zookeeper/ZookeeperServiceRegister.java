package com.xk.rpccore.register.zookeeper;

import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.netcommon.ServiceInfo;
import com.xk.rpccore.register.ServiceRegister;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * @author xk
 * @date 2024/8/27--18:47
 * 基于 Zookeeper 的服务注册中心。通过 Curator Framework，封装了服务的注册、注销和销毁的功能
 */
@Slf4j
public class ZookeeperServiceRegister implements ServiceRegister {
    //会话超时时间
    private static final int SESSION_TIMEOUT = 60 * 1000;
    //连接超时时间
    private static final int CONNECT_TIMEOUT = 15 * 1000;
    //重试策略中基础的等待时间
    private static final int BASE_SLEEP_TIME = 3 * 1000;
    //最大重试次数
    private static final int MAX_RETRY = 10;
    //存储服务信息的基础路径
    private static final String BASE_PATH = "/xk_rpc";
    //Curator Framework 客户端，用于与 Zookeeper 交互
    private CuratorFramework client;
    //Curator 提供的服务发现组件，管理服务的注册与发现
    private ServiceDiscovery<ServiceInfo> serviceDiscovery;
    
    public ZookeeperServiceRegister(String registryAddress){
        try {
            client = CuratorFrameworkFactory
                    .newClient(registryAddress, SESSION_TIMEOUT, CONNECT_TIMEOUT,
                            new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRY));
            // 开启客户端通信
            client.start();

            // 构建 ServiceDiscovery 服务注册中心
            serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .serializer(new JsonInstanceSerializer<>(ServiceInfo.class))
                    .basePath(BASE_PATH)
                    .build();

            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {

        try {
            ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                    .name(serviceInfo.getServiceName())
                    .address(serviceInfo.getAddress())
                    .port(serviceInfo.getPort())
                    .payload(serviceInfo)
                    .build();
            serviceDiscovery.registerService(serviceInstance);
            log.info("Successfully registered [{}] service.", serviceInstance.getName());
        } catch (Exception e) {
            throw new RpcException(String.format("An error occurred when rpc server registering [%s] service.",
                    serviceInfo.getServiceName()), e);
        }
        
    }

    @Override
    public void unregister(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
        log.warn("Successfully unregistered {} service.", serviceInstance.getName());
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
        client.close();
        log.info("Destroy zookeeper registry completed.");
    }
}
