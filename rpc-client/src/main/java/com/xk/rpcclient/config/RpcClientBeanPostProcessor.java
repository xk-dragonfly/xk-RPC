package com.xk.rpcclient.config;

import com.xk.rpcclient.annotation.RpcClient;
import com.xk.rpcclient.proxy.ClientProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @author xk
 * @date 2024/8/19--23:02
 */
public class RpcClientBeanPostProcessor implements BeanPostProcessor {
    private final ClientProxyFactory proxyFactory;

    public RpcClientBeanPostProcessor(ClientProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取该 bean 的类的所有属性（getFields - 获取所有的public属性，getDeclaredFields - 获取所有声明的属性，不区分访问修饰符）
        Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历所有属性
        for (Field field : fields) {
            // 判断是否被 RpcReference 注解标注
            if (field.isAnnotationPresent(RpcClient.class)) {
                // 获得 RpcReference 注解
                RpcClient rpcReference = field.getAnnotation(RpcClient.class);
                // 默认类为属性当前类型
                // filed.class = java.lang.reflect.Field
                // filed.type = com.xk.xxx.service.XxxService
                Class<?> clazz = field.getType();
                try {
                    // 如果指定了全限定类型接口名
                    if (!"".equals(rpcReference.interfaceName())) {
                        clazz = Class.forName(rpcReference.interfaceName());
                    }
                    // 如果指定了接口类型
                    if (rpcReference.interfaceClass() != void.class) {
                        clazz = rpcReference.interfaceClass();
                    }
                    // 获取指定类型的代理对象
                    Object proxy = proxyFactory.getProxy(clazz, rpcReference.version());
                    // 关闭安全检查
                    field.setAccessible(true);
                    // 设置域的值为代理对象
                    field.set(bean, proxy);
                } catch (ClassNotFoundException | IllegalAccessException e) {
                    throw new RuntimeException(String.format("Failed to obtain proxy object, the type of field %s is %s, " +
                            "and the specified loaded proxy type is %s.", field.getName(), field.getClass(), clazz), e);
                }
            }
        }
        return bean;
    }
}
