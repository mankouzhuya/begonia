package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.github.begonia.core.bus.jvm.disruptor.queue.ValueWrapper;
import com.github.begonia.core.context.MethodNode;

public class MethodNodeEvent extends ValueWrapper<MethodNode> {

    @Override
    public String toString() {
        return getValue().toString();
    }
}