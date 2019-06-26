package com.github.begonia.examples.springboot.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author xujin
 */
@SpringBootApplication
public class HelloProviderApplication {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        SpringApplication.run(HelloProviderApplication.class, args);
//        //regist bus
//        Bus.register(new Eventhandler());
//        Client client = new Client();
//        client.start("localhost", 8888);
    }


}
