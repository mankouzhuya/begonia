package com.github.begonia.cache;

public class KeyHandle implements Runnable{

    private String key;

    public KeyHandle(String key){
        this.key = key;
    }

    @Override
    public void run() {
        DefaultCache.getInstance().remove(key);
    }
}
