package com.github.begonia.core.bus;

import com.alibaba.fastjson.JSON;
import com.github.begonia.core.bus.dto.MthodDTO;
import com.github.begonia.core.cache.DefaultCache;
import com.github.begonia.core.cache.SimpleCache;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Msghandler {


    /**
     * 只有通过@Subscribe注解的方法才会被注册进EventBus
     * 而且方法有且只能有1个参数
     *
     * @param reqDTO
     */
    @Subscribe
    public void process(MthodDTO reqDTO) {
        String json = JSON.toJSONString(reqDTO);
        SimpleCache simpleCache = DefaultCache.getInstance();
        simpleCache.put(reqDTO.getMthodId(), json);
    }
}
