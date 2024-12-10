package com.xk.rpccore.extension.factory;


import com.xk.rpccore.extension.ExtensionFactory;
import com.xk.rpccore.extension.ExtensionLoader;
import com.xk.rpccore.extension.SPI;

/**
 * @author xk
 * @version 1.0
 * @ClassName SpiExtensionFactory
 * @Date 2024/12/3 22:33
 */
public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(Class<?> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<?> extensionLoader = ExtensionLoader.getExtensionLoader(type);
            // todo: implement this method
        }
        return null;
    }
}
