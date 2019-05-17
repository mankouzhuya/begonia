package com.github.begonia.core.fun;

@FunctionalInterface
public interface UncheckedFunction2<T, R> {
    R apply(T t) throws Exception;
}