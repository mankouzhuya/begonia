package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.lmax.disruptor.EventFactory;

public class MethodNodeEventFactory implements EventFactory<MethodNodeEvent> {


    @Override
    public MethodNodeEvent newInstance() {
        return new MethodNodeEvent();
    }
}