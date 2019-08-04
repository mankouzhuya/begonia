package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.github.begonia.core.bus.jvm.disruptor.queue.BaseQueue;
import com.github.begonia.core.bus.jvm.msg.Msg;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;

import java.util.List;

/***
 * method node queue
 * **/
public class MsgQueue extends BaseQueue<Msg, MsgEvent, MsgEventHandler> {

    private static final int QUEUE_SIZE = 1024 * 1024;


    private List<MsgEventHandler> handlers;

    public MsgQueue(List<MsgEventHandler> handlers) {
        this.handlers = handlers;
        this.init();
    }


    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory eventFactory() {
        return new MsgEventFactory();
    }

    @Override
    protected WorkHandler[] getHandler() {
        int size = handlers.size();
        return handlers.toArray(new MsgEventHandler[size]);
    }

    @Override
    protected WaitStrategy getStrategy() {
//        return new BlockingWaitStrategy();
        return new SleepingWaitStrategy();
    }
}