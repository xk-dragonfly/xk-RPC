package com.xk.rpcserver.config;

import com.xk.rpccore.netcommon.ServiceInfo;
import com.xk.rpccore.register.ServiceRegister;
import com.xk.rpccore.util.ServiceUtil;
import com.xk.rpcserver.annotation.RpcService;
import com.xk.rpcserver.cache.LocalServiceCache;
import com.xk.rpcserver.transmission.TransServer;
import com.xk.rpcserver.transmission.common.RpcServerProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

/**
 * @author xk
 * @date 2024/8/24--15:23
 * 用于注册和暴露服务
 */
@Slf4j
public class RpcServerBeanPostProcessor implements BeanPostProcessor, CommandLineRunner {
    private final ServiceRegister serviceRegister;

    private final TransServer transServer;

    private final RpcServerProperties properties;
    
    public RpcServerBeanPostProcessor(ServiceRegister serviceRegister, TransServer transServer,RpcServerProperties properties) {
        this.serviceRegister = serviceRegister;
        this.transServer = transServer;
        this.properties = properties;
    }

    /**
     * 在 bean 实例化后，初始化后，检测标注有 @RpcService 注解的类，将对应的服务类进行注册，对外暴露服务，同时进行本地服务注册
     */
    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 判断当前 bean 是否被 @RpcService 注解标注
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with [{}].", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // 获取到该类的 @RpcService 注解
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            String interfaceName;
            if ("".equals(rpcService.interfaceName())) {
                interfaceName = rpcService.interfaceClass().getName();
            } else {
                interfaceName = rpcService.interfaceName();
            }
            String version = rpcService.version();
            String serviceName = ServiceUtil.serviceKey(interfaceName, version);
            // 构建 ServiceInfo 对象
            ServiceInfo serviceInfo = ServiceInfo.builder()
                    .appName(properties.getAppName())
                    .serviceName(serviceName)
                    .version(version)
                    .address(properties.getAddress())
                    .port(properties.getPort())
                    .build();
            // 进行远程服务注册
            serviceRegister.register(serviceInfo);
            // 进行本地服务缓存注册
            LocalServiceCache.addService(serviceName, bean);
        }
        return bean;
    }
    

    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> transServer.start(properties.getPort())).start();
        log.info("Rpc server [{}] start, the appName is {}, the port is {}",
                transServer, properties.getAppName(), properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 当服务关闭之后，将服务从 注册中心 上清除（关闭连接）
                serviceRegister.destroy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
