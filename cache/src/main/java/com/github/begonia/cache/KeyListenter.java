package com.github.begonia.cache;

@FunctionalInterface
public interface KeyListenter {

    void onExpire(String key,Object value);

}
