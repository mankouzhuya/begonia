package com.github.begonia.core.fun;

@FunctionalInterface
public interface UncheckedFunctionArg2<T, R> {
    R apply(T t) throws Exception;
}