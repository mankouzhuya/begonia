package com.github.begonia.core.context;

import com.github.begonia.core.bus.jvm.disruptor.method_queue.MsgEventHandler;
import com.github.begonia.core.bus.jvm.disruptor.method_queue.MsgQueue;
import com.github.begonia.core.bus.jvm.msg.Msg;

import java.util.Arrays;

public class PostMan {

    private static MsgQueue queue;

    private PostMan() {

    }

    public static MsgQueue getInstance() {
        if (queue == null) {
            synchronized (PostMan.class) {
                if (queue == null) queue = new MsgQueue(Arrays.asList(new MsgEventHandler()));
            }
        }
        return queue;
    }


    public static void push(Msg msg) {
        getInstance().publishEvent(msg);
    }

    public static void close() {
        getInstance().shutdown();
    }

}
