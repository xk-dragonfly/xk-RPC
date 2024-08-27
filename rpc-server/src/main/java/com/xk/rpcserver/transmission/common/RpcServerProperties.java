package com.xk.rpcserver.transmission.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xk
 * @date 2024/8/24--14:51
 */
@Data
@ConfigurationProperties(prefix = "rpc.server")
public class RpcServerProperties {
    //服务提供地址
    private String address;
    //服务提供端口
    private Integer port;
    //服务名称
    private String appName;
    //服务注册方式
    private String register;
    //服务提供方式
    private String transmission;
    //服务注册地址
    private String registerAddr;

    public RpcServerProperties() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostAddress();
        this.port = 8080;
        this.appName = "provider-1";
        this.register = "zookeeper";
        this.transmission = "netty";
        this.registerAddr = "127.0.0.1:2181";
    }
}
