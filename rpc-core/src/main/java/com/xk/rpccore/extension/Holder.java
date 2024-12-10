package com.xk.rpccore.extension;

/**
 * Holder 类，作用是为不可变的对象引用提供一个可变的包装
 *
 * @author xk
 * @version 1.0
 * @ClassName Holder
 * @Date 2024/12/1 19:01
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
