package com.github.begonia.examples.springboot.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xujin
 */
@SpringBootApplication
public class HelloProviderApplication {

    public static void main(String[] args) {
//        System.out.println(Thread.currentThread().getStackTrace()[1].getClassName());
//        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
//        new HelloProviderApplication().test();
        SpringApplication.run(HelloProviderApplication.class, args);
    }

    public void test(){

        List list = new ArrayList();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        System.out.println(Thread.currentThread().getStackTrace()[1].getClassName());
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    public String test2(){
        try {
            return "";
        }finally {
            test();
        }
    }

}
