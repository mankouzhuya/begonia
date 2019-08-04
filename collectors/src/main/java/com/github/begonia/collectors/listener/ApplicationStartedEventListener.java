package com.github.begonia.collectors.listener;

import com.github.begonia.collectors.support.processor.MsgProcessor;
import com.github.begonia.communication.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationStartedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${socket.host}")
    private String host;

    @Value("${socket.port}")
    private Integer port;

    @Autowired
    private MsgProcessor msgProcessor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("容器初始化完毕");
        try {
            Server.start(host, port, msgProcessor);
        } catch (Exception e) {
            log.error("远程通讯模块启动失败", e);
        }
    }
}
