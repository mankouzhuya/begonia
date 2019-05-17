package com.github.begonia.examples.springboot.provider.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelloService {


    public String hello3333(String msg){
        return LocalDateTime.now()+ "_"+msg;
    }
}
