package com.xk.rpcclient.config;

import com.xk.rpccore.discover.ServiceDiscover;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author xk
 * @date 2024/8/19--23:56
 */
@Slf4j
public class RpcClientExitDisposableBean implements DisposableBean {
    /**
     * 服务发现中心
     */
    private final ServiceDiscover serviceDiscover;

    public RpcClientExitDisposableBean(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    /**
     * 客户端退出时执行的一些额外操作（关闭资源、连接等）
     *
     * @throws Exception 异常
     */
    @Override
    public void destroy() throws Exception {
        try {
            if (serviceDiscover != null) {
                serviceDiscover.destroy();
            }
            log.info("Rpc client resource release completed and exited successfully.");
        } catch (Exception e) {
            log.warn("An exception occurred while executing the destroy operation when the rpc client exited, {}.",
                    e.getMessage());
        }
    }
}
