package com.xk.rpcclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
}
