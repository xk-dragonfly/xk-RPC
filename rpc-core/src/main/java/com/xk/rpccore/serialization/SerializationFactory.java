package com.xk.rpccore.serialization;

import com.xk.rpccore.constant.SerializationType;
import com.xk.rpccore.serialization.jdk.JdkSerialization;
import com.xk.rpccore.serialization.json.JsonSerialization;

/**
 * @author xk
 * @date 2024/8/28--13:37
 */
public class SerializationFactory {
    public static Serialization getSerialization(SerializationType enumType) {
        switch (enumType) {
            case JDK:
                return new JdkSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException(String.format("The serialization type %s is illegal.",
                        enumType.name()));
        }
    }
}
