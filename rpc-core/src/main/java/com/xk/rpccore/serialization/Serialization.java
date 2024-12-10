package com.xk.rpccore.serialization;

import com.xk.rpccore.extension.SPI;

/**
 * @author xk
 * @date 2024/8/28--13:36
 */
@SPI
public interface Serialization {
    
    //序列化
    <T> byte[] serialize(T object);

    //反序列化
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
