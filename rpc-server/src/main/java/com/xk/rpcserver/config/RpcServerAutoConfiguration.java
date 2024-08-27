package com.xk.rpcserver.config;

import com.xk.rpcserver.transmission.common.RpcServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xk
 * @date 2024/8/24--14:52
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {
    
    @Autowired
    RpcServerProperties properties;
    
    
}
