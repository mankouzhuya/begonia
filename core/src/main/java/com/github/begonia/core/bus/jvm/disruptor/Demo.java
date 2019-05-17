package com.github.begonia.core.bus.jvm.disruptor;

import com.github.begonia.core.bus.jvm.disruptor.method_queue.MethodNodeEventHandler;
import com.github.begonia.core.bus.jvm.disruptor.method_queue.MethodNodeQueue;
import com.github.begonia.core.bus.jvm.event_bus.Bus;
import com.github.begonia.core.bus.jvm.event_bus.Eventhandler;
import com.github.begonia.core.context.MethodNode;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Demo {

    public static void main(String[] args) {
        MethodNodeEventHandler methodNodeEventHandler = new MethodNodeEventHandler();
        List<MethodNodeEventHandler> handlers = new ArrayList<>();
        handlers.add(methodNodeEventHandler);
        MethodNodeQueue methodNodeQueue = new MethodNodeQueue(handlers);
        Long start = System.currentTimeMillis();
        Bus.register(new Eventhandler());


        Executor executor = Executors.newFixedThreadPool(10);
        AsyncEventBus asyncEventBus = new AsyncEventBus("asyncEventBus", executor);
        /**
         * 注册事件处理器
         */
        asyncEventBus.register(new Object(){

            //@AllowConcurrentEvents
            @Subscribe
            public void handleUserInfoChangeEvent(String string){
                System.out.println("所有事件的父类:"+string);
            }

        });


        for (int i = 0; i < 10; i++) {
            MethodNode methodNode = new MethodNode();
            methodNode.setFullClassName("我是测试方法"+i);
//            asyncEventBus.post(methodNode.getFullClassName());
//            methodNodeQueue.publishEvent(methodNode);
            Bus.post(methodNode);
        }






        System.out.println(System.currentTimeMillis() - start);
        methodNodeQueue.shutdown();


    }
}
