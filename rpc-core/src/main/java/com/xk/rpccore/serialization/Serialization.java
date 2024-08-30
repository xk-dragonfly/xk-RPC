package com.xk.rpccore.serialization;

/**
 * @author xk
 * @date 2024/8/28--13:36
 */
public interface Serialization {
    
    //序列化
    <T> byte[] serialize(T object);

    //反序列化
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
