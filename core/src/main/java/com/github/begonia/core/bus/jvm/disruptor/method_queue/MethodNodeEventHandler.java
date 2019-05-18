package com.github.begonia.core.bus.jvm.disruptor.method_queue;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

//EventHandler<MethodNodeEvent>,
@Slf4j
public class MethodNodeEventHandler implements  WorkHandler<MethodNodeEvent> ,EventHandler<MethodNodeEvent>{

    public MethodNodeEventHandler() {

    }

    @Override
    public synchronized void onEvent(MethodNodeEvent event) {
        if (event == null || event.getValue() == null) {
            log.warn("收到消息为空!");
            return;
        }
        if (log.isDebugEnabled()) log.debug("处理消息:{}", event.getValue().toString());
//        log.info("处理消息:{}", event.getValue().toString());
//        MethodNode node = event.getValue();
//        Object object = DefaultCache.getInstance().get(node.getTrackId());
//        if(object != null){
//            List<MethodNode> arrayList = (List<MethodNode>) object;
//            arrayList.add(node);
//            DefaultCache.getInstance().put(node.getTrackId(),arrayList);
//            return ;
//        }
//        List<MethodNode> arrayList = new ArrayList<>();
//        arrayList.add(node);
//        DefaultCache.getInstance().put(node.getTrackId(),arrayList);
    }

    @Override
    public void onEvent(MethodNodeEvent methodNodeEvent, long l, boolean b) throws Exception {
        if(log.isDebugEnabled()) log.debug("接受到消息:" + methodNodeEvent.toString());
        this.onEvent(methodNodeEvent);
    }
}