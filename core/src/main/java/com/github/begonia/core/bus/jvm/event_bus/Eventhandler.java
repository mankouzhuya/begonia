package com.github.begonia.core.bus.jvm.event_bus;

import com.github.begonia.core.cache.DefaultCache;
import com.github.begonia.core.context.MethodNode;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Eventhandler {


    /**
     * 只有通过@Subscribe注解的方法才会被注册进EventBus
     * 而且方法有且只能有1个参数
     *
     * @param node
     */
   // @AllowConcurrentEvents//线程安全
    @Subscribe
    public void process(MethodNode node) {
        log.info("处理消息:{}", node.toString());
        Object object = DefaultCache.getInstance().get(node.getTrackId());
        if(object != null){
            List<MethodNode> arrayList = (List<MethodNode>) object;
            arrayList.add(node);
            DefaultCache.getInstance().put(node.getTrackId(),arrayList);
            return ;
        }
        List<MethodNode> arrayList = new ArrayList<>();
        arrayList.add(node);
        DefaultCache.getInstance().put(node.getTrackId(),arrayList);
    }
}
