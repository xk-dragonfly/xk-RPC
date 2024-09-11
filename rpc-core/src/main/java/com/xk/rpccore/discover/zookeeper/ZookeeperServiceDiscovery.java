package com.xk.rpccore.discover.zookeeper;

import com.xk.rpccore.exception.RpcException;
import com.xk.rpccore.discover.ServiceDiscover;
import com.xk.rpccore.loadbalance.LoadBalance;
import com.xk.rpccore.netcommon.RpcRequest;
import com.xk.rpccore.netcommon.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xk
 * @date 2024/8/27--20:41
 */
@Slf4j
public class ZookeeperServiceDiscovery implements ServiceDiscover {
    
    //会话超时时间
    private static final int SESSION_TIMEOUT = 60 * 1000;
    //连接超时时间
    private static final int CONNECT_TIMEOUT = 15 * 1000;
    //连接失败后重试策略，使用指数回退策略
    private static final int BASE_SLEEP_TIME = 3 * 1000;

    private static final int MAX_RETRY = 10;

    private static final String BASE_PATH = "/xk_rpc";
    //负载均衡策略
    private LoadBalance loadBalance;
    //zookeeper通信客户端
    private CuratorFramework client;
    //服务注册和发现对象
    private org.apache.curator.x.discovery.ServiceDiscovery<ServiceInfo> serviceDiscovery;

    /**
     * ServiceCache: 将在zk中的服务数据缓存至本地，并监听服务变化，实时更新缓存
     * 服务本地缓存，将服务缓存到本地并增加 watch 事件，当远程服务发生改变时自动更新服务缓存
     */
    private final Map<String, ServiceCache<ServiceInfo>> serviceCacheMap = new ConcurrentHashMap<>();

    /**
     * 用来将服务列表缓存到本地内存，当服务发生变化时，由 serviceCache 进行服务列表更新操作，当 zk 挂掉时，将保存当前服务列表以便继续提供服务
     */
    private final Map<String, List<ServiceInfo>> serviceMap = new ConcurrentHashMap<>();

    /**
     * 构造函数 ZookeeperServiceDiscovery 接收 registerAddr（Zookeeper 注册中心地址）和 loadBalance（负载均衡策略）作为参数
     * 初始化 Zookeeper 客户端并启动服务发现，通过 ServiceDiscoveryBuilder 创建服务注册中心对象 serviceDiscovery，并启动服务发现
     */
    public ZookeeperServiceDiscovery(String registerAddr,LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        
        client = CuratorFrameworkFactory.newClient(registerAddr,SESSION_TIMEOUT,CONNECT_TIMEOUT,new ExponentialBackoffRetry(BASE_SLEEP_TIME,MAX_RETRY));
        client.start();

        // 构建 ServiceDiscovery 服务注册中心
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(client)
                .serializer(new JsonInstanceSerializer<>(ServiceInfo.class))
                .basePath(BASE_PATH)
                .build();
        
        // 开启 服务发现
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServiceInfo discover(RpcRequest request) {
        try {
            return loadBalance.select(getServices(request.getServiceName()), request);
        } catch (Exception e) {
            throw new RpcException(String.format("Remote service discovery did not find service %s.",
                    request.getServiceName()), e);
        }
    }
    
    @Override
    public List<ServiceInfo> getServices(String serviceName) throws Exception {
        if (!serviceCacheMap.containsKey(serviceName)) {
            // 构建本地服务缓存
            ServiceCache<ServiceInfo> serviceCache = serviceDiscovery.serviceCacheBuilder()
                    .name(serviceName)
                    .build();
            // 添加服务监听，当服务发生变化时主动更新本地缓存并通知
            serviceCache.addListener(new ServiceCacheListener() {
                @Override
                public void cacheChanged() {
                    log.info("The service [{}] cache has changed. The current number of service samples is {}."
                            , serviceName, serviceCache.getInstances().size());
                    // 更新本地缓存的服务列表
                    serviceMap.put(serviceName, serviceCache.getInstances().stream()
                            .map(ServiceInstance::getPayload)
                            .collect(Collectors.toList()));
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    // 当连接状态发生改变时，只打印提示信息，保留本地缓存的服务列表
                    log.info("The client {} connection status has changed. The current status is: {}."
                            , client, newState);
                }
            });
            // 开启服务缓存监听
            serviceCache.start();
            // 将服务缓存对象存入本地
            serviceCacheMap.put(serviceName, serviceCache);
            // 将服务列表缓存到本地
            serviceMap.put(serviceName, serviceCacheMap.get(serviceName).getInstances()
                    .stream()
                    .map(ServiceInstance::getPayload)
                    .collect(Collectors.toList()));
        }
        return serviceMap.get(serviceName);
    }

    @Override
    public void destroy() throws Exception {
        for (ServiceCache<ServiceInfo> serviceCache : serviceCacheMap.values()) {
            if (serviceCache != null) {
                serviceCache.close();
            }
        }
        if (serviceDiscovery != null) {
            serviceDiscovery.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
