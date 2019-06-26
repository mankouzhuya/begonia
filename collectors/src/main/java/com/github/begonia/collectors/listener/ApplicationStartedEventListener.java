package com.github.begonia.collectors.listener;

import com.github.begonia.communication.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationStartedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("容器初始化完毕");
        try {
            Server.start("localhost", 8888);
        } catch (Exception e) {
            log.error("远程通讯模块启动失败", e);
        }
    }
}
