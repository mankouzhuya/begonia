package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.lmax.disruptor.EventFactory;

public class MsgEventFactory implements EventFactory<MsgEvent> {


    @Override
    public MsgEvent newInstance() {
        return new MsgEvent();
    }
}