package com.github.begonia.core.context;

import com.github.begonia.core.bus.jvm.disruptor.method_queue.MethodNodeEventHandler;
import com.github.begonia.core.bus.jvm.disruptor.method_queue.MethodNodeQueue;

import java.util.Arrays;

public class PostMan {

    private static MethodNodeQueue queue;

    private PostMan(){

    }

    public static MethodNodeQueue getInstance(){
        if(queue == null){
            synchronized (PostMan.class){
                if(queue == null) queue = new MethodNodeQueue(Arrays.asList(new MethodNodeEventHandler(),new MethodNodeEventHandler()));
            }
        }
        return queue;
    }




    public static void push(MethodNode methodNode){
        getInstance().publishEvent(methodNode);
    }

    public static void close(){
        getInstance().shutdown();
    }

}
