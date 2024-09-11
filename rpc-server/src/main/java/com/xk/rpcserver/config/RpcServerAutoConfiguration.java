package com.xk.rpcserver.config;

import com.xk.rpccore.register.ServiceRegister;
import com.xk.rpccore.register.zookeeper.ZookeeperServiceRegister;
import com.xk.rpcserver.transmission.TransServer;
import com.xk.rpcserver.transmission.common.RpcServerProperties;
import com.xk.rpcserver.transmission.netty.NettyServer;
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
 * @date 2024/8/24--14:52
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {
    
    @Autowired
    RpcServerProperties properties;

    @Bean(name = "serviceRegister")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server", name = "register", havingValue = "zookeeper", matchIfMissing = true)
    public ZookeeperServiceRegister zookeeperServiceRegister() {
        return new ZookeeperServiceRegister(properties.getRegisterAddr());
    }

    @Bean(name = "rpcServer")
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.server", name = "transmission", havingValue = "netty", matchIfMissing = true)
    public TransServer nettyRpcServer() {
        return new NettyServer();
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({ServiceRegister.class, TransServer.class})
    public RpcServerBeanPostProcessor rpcServerBeanPostProcessor(@Autowired ServiceRegister serviceRegister,
                                                                 @Autowired TransServer transServer,
                                                                 @Autowired RpcServerProperties properties) {

        return new RpcServerBeanPostProcessor(serviceRegister, transServer, properties);
    }
    
}
