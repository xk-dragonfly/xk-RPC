package com.xk.consumer.controller;

import com.xk.common.service.HelloService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xk
 * @date 2024/8/17--15:00
 */
@RestController
@RequestMapping
public class HelloController {

    private HelloService helloService;


    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {

        return helloService.sayHello(name);
    }
}
