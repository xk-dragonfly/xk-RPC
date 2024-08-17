package com.xk.provider.service.impl;

import com.xk.common.service.HelloService;

/**
 * @author xk
 * @date 2024/8/17--16:01
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
