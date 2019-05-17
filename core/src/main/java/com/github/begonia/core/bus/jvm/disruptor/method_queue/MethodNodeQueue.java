package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.github.begonia.core.bus.jvm.disruptor.queue.BaseQueue;
import com.github.begonia.core.context.MethodNode;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import java.util.List;

/***
 * method node queue
 * **/
public class MethodNodeQueue extends BaseQueue<MethodNode, MethodNodeEvent, MethodNodeEventHandler> {

    private static final int QUEUE_SIZE = 1024 * 1024;


    private List<MethodNodeEventHandler> handlers;

    public MethodNodeQueue(List<MethodNodeEventHandler> handlers){
        this.handlers = handlers;
        this.init();
    }


    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory eventFactory() {
        return new MethodNodeEventFactory();
    }

    @Override
    protected WorkHandler[] getHandler() {
        int size = handlers.size();
        return handlers.toArray(new MethodNodeEventHandler[size]);
    }

    @Override
    protected WaitStrategy getStrategy() {
//        return new BlockingWaitStrategy();
        return new SleepingWaitStrategy();
    }
}