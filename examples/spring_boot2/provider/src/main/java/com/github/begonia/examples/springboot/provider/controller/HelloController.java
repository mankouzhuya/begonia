package com.github.begonia.examples.springboot.provider.controller;

import com.github.begonia.examples.springboot.provider.dto.UserDTO;
import com.github.begonia.examples.springboot.provider.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author: xujin
 **/
@RequestMapping("/hello")
@RestController
public class HelloController {


    @Autowired
    private HelloService helloService;

    @GetMapping("")
    public String hello(@RequestParam(value = "name") String name){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();//stack trace
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement traceElements = stackTrace[i];
            String className = traceElements.getClassName();
            traceElements.getFileName();
            traceElements.getMethodName();
            traceElements.getLineNumber();
            System.out.println(className);
        }
        helloService.hello3333("你好");
        return "name->"+name+",date:"+ LocalDateTime.now().toString();
    }

    @PostMapping
    public UserDTO hello2(@RequestParam(value = "name") String name, @RequestBody UserDTO userDTO){
        userDTO.setHello("你好,现在是北京时间:"+ LocalDateTime.now().toString());
        return userDTO;
    }


    @GetMapping("/test")
    public String hello3(@RequestParam(value = "name") String name){
        return helloService.hello3333(name);
    }

}
