package com.xk.provider.service.impl;

import com.xk.common.service.HelloService;
import com.xk.rpcserver.annotation.RpcService;

/**
 * @author xk
 * @date 2024/8/17--16:01
 */
@RpcService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
