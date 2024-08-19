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

}
