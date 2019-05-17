package com.github.begonia.core.fun;

@FunctionalInterface
public interface UncheckedConsumer<T> {

    void accept(T t) throws Exception;

}