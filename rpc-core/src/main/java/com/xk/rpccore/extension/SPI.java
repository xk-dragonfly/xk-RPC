package com.xk.rpccore.extension;

import java.lang.annotation.*;

/**
 * SPI 注解，被标注的类表示为需要加载的扩展类接口
 *
 * @author xk
 * @version 1.0
 * @ClassName SPI
 * @Date 2024/12/1 19:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {

}
