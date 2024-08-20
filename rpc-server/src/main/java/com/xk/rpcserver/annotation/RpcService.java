package com.xk.rpcserver.annotation;

import java.lang.annotation.*;

/**
 * @author xk
 * @date 2024/8/20--22:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcService {
    
    //对外暴露服务的接口类型
    Class<?> interfaceClass() default void.class;

    //对外暴露服务的接口名（全限定名）
    String interfaceName() default "";

    String version() default "1.0";
}
