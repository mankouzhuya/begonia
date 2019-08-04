package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.github.begonia.core.bus.jvm.disruptor.queue.ValueWrapper;
import com.github.begonia.core.bus.jvm.msg.Msg;

public class MsgEvent extends ValueWrapper<Msg> {

    @Override
    public String toString() {
        return getValue().toString();
    }
}