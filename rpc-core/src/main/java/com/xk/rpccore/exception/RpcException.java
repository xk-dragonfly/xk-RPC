package com.xk.rpccore.exception;

/**
 * @author xk
 * @date 2024/8/24--15:08
 */
public class RpcException extends RuntimeException{
    private static final long serialVersionUID = 3365624081242234231L;

    public RpcException() {
        super();
    }

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
