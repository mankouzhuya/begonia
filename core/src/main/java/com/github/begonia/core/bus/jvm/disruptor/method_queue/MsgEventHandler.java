package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.github.begonia.core.bus.jvm.event_bus.Eventhandler;
import com.github.begonia.core.bus.jvm.msg.Msg;
import com.github.begonia.core.context.MethodNode;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

//EventHandler<MethodNodeEvent>,
@Slf4j
public class MsgEventHandler implements  WorkHandler<MsgEvent> ,EventHandler<MsgEvent>{

    public MsgEventHandler() {

    }

    @Override
    public void onEvent(MsgEvent event) throws InterruptedException, ExecutionException, IOException {
        if (event == null || event.getValue() == null) {
            log.warn("收到消息为空!");
            return;
        }
        if (log.isDebugEnabled()) log.debug("处理消息:{}", event.getValue().toString());
        Msg msg = event.getValue();
        new Eventhandler().process(msg);
    }

    @Override
    public void onEvent(MsgEvent methodNodeEvent, long l, boolean b) throws Exception {
        if(log.isDebugEnabled()) log.debug("接受到消息:" + methodNodeEvent.toString());
        this.onEvent(methodNodeEvent);
    }
}