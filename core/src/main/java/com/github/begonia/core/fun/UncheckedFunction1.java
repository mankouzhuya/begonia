package com.github.begonia.core.fun;

@FunctionalInterface
public interface UncheckedFunction1<R> {
    R apply() throws Exception;
}