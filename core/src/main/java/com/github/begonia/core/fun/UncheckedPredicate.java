package com.github.begonia.core.fun;

@FunctionalInterface
public interface UncheckedPredicate<T> {

    boolean test(T t)throws Exception;

}