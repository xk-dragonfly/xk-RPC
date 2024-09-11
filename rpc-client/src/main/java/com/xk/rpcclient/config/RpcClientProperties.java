package com.xk.rpcclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xk
 * @date 2024/8/19--23:41
 */
@Data
@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientProperties {
    
    private String transmission;
    
    private String serialization;

    private String loadbalance;

    private String registry;

    private String registryAddr;

    private Integer timeout;

    public RpcClientProperties() {
        this.loadbalance = "random";
        this.serialization = "HESSIAN";
        this.transmission = "netty";
        this.registry = "zookeeper";
        this.registryAddr = "127.0.0.1:2181";
        this.timeout = 5000;
    }
    
}
