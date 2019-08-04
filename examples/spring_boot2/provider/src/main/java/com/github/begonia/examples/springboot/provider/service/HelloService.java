package com.github.begonia.examples.springboot.provider.service;

import com.github.begonia.examples.springboot.provider.feign.HelloFeignService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelloService {

    @Autowired
    private HelloFeignService2 helloFeignService2;

    public String hello3333(String msg) {
        h1();
        helloFeignService2.hello(msg);
        h2();
        return LocalDateTime.now() + "_" + msg;
    }


    public void h1() {
        System.out.println("h1执行了");
    }

    public void h2() {
        System.out.println("h2执行了");
    }
}
