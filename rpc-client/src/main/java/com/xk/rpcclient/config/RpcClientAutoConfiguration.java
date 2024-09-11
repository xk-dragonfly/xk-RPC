package com.xk.rpcclient.config;

import com.xk.rpcclient.proxy.ClientProxyFactory;
import com.xk.rpcclient.transmission.TransClient;
import com.xk.rpcclient.transmission.netty.NettyTransClient;
import com.xk.rpccore.discover.ServiceDiscover;
import com.xk.rpccore.discover.zookeeper.ZookeeperServiceDiscovery;
import com.xk.rpccore.loadbalance.LoadBalance;
import com.xk.rpccore.loadbalance.impl.RandomLoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author xk
 * @date 2024/8/19--23:41
 *
 */
@Configuration
@EnableConfigurationProperties(RpcClientProperties.class)
public class RpcClientAutoConfiguration {
    @Autowired
    RpcClientProperties rpcClientProperties;

    @Bean(name = "loadBalance")
    @Primary
    @ConditionalOnMissingBean // 不指定 value 则值默认为当前创建的类
    @ConditionalOnProperty(prefix = "rpc.client", name = "loadbalance", havingValue = "random", matchIfMissing = true)
    public LoadBalance randomLoadBalance() {
        return new RandomLoadBalance();
    }

    @Bean(name = "serviceDiscovery")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnBean(LoadBalance.class)
    @ConditionalOnProperty(prefix = "rpc.client", name = "registry", havingValue = "zookeeper", matchIfMissing = true)
    public ServiceDiscover zookeeperServiceDiscovery(@Autowired LoadBalance loadBalance) {
        return new ZookeeperServiceDiscovery(rpcClientProperties.getRegistryAddr(), loadBalance);
    }


    @Bean(name = "rpcClient")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "transport", havingValue = "netty", matchIfMissing = true)
    public TransClient nettyRpcClient() {
        return new NettyTransClient();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({ServiceDiscover.class, TransClient.class})
    public ClientProxyFactory clientStubProxyFactory(@Autowired ServiceDiscover serviceDiscover,
                                                     @Autowired TransClient transClient,
                                                     @Autowired RpcClientProperties rpcClientProperties) {
        return new ClientProxyFactory(serviceDiscover, transClient, rpcClientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientBeanPostProcessor rpcClientBeanPostProcessor(@Autowired ClientProxyFactory clientProxyFactory) {
        return new RpcClientBeanPostProcessor(clientProxyFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientExitDisposableBean rpcClientExitDisposableBean(@Autowired ServiceDiscover serviceDiscover) {
        return new RpcClientExitDisposableBean(serviceDiscover);
    }
}
