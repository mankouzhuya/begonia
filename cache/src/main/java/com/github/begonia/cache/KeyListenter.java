package com.github.begonia.cache;

@FunctionalInterface
public interface KeyListenter {

    String onExpire(String key);

}
