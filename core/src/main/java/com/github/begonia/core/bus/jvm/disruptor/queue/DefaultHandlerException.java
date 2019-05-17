package com.github.begonia.core.bus.jvm.disruptor.queue;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultHandlerException implements ExceptionHandler {


    /*
     * (non-Javadoc) 运行过程中发生时的异常
     *
     * @see
     * com.lmax.disruptor.ExceptionHandler#handleEventException(java.lang.Throwable
     * , long, java.lang.Object)
     */
    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        log.error("处理队列异常:sequence->{},event->{},ex ->{}", sequence, event.toString(), ex);
    }

    /*
     * (non-Javadoc) 启动时的异常
     *
     * @see
     * com.lmax.disruptor.ExceptionHandler#handleOnStartException(java.lang.
     * Throwable)
     */
    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("启动队列异常:{}", ex.getMessage());
    }

    /*
     * (non-Javadoc) 关闭时的异常
     *
     * @see
     * com.lmax.disruptor.ExceptionHandler#handleOnShutdownException(java.lang
     * .Throwable)
     */
    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("关闭队列异常:{}",ex.getMessage());
    }

}