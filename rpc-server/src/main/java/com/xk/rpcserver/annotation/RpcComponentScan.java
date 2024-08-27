package com.xk.rpcserver.annotation;

import com.xk.rpcserver.config.RpcBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author xk
 * @date 2024/8/24--15:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RpcBeanDefinitionRegistrar.class)
public @interface RpcComponentScan {
    /**
     * 扫描包路径
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * 扫描包路径
     */
    @AliasFor("value")
    String[] basePackages() default {};
}
