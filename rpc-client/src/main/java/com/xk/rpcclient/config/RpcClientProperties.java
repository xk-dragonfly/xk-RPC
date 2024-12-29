package com.xk.rpcclient.config;

import com.xk.rpccore.retry.RetryStrategy;
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

    private String loadBalance;

    private String register;

    private String registerAddr;

    private Integer timeout;
    
    private String retryStrategy;

    private Boolean enableSsl;

    public RpcClientProperties() {
        this.loadBalance = "random";
        this.serialization = "HESSIAN";
        this.transmission = "netty";
        this.register = "zookeeper";
        this.registerAddr = "127.0.0.1:2181";
        this.timeout = 5000;
        this.retryStrategy = "randomRetryStrategy";
        this.enableSsl = false;
    }
    
}
