package com.xk.rpcclient.annotation;

import java.lang.annotation.*;

/**
 * @author xk
 * @date 2024/8/19--22:57
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcClient {
    /**
     * 对外暴露服务的接口类型，默认为 void.class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 对外暴露服务的接口名（全限定名），默认为 ""
     */
    String interfaceName() default "";

    /**
     * 版本号，默认 1.0
     */
    String version() default "1.0";

    /**
     * 负载均衡策略，合法的值包括：random, roundrobin, leastactive
     */
    String loadbalance() default "";

    /**
     * 服务调用超时时间
     */
    int timeout() default 0;
}
