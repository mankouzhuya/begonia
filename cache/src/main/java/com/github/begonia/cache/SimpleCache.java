package com.github.begonia.cache;

import java.util.List;
import java.util.Map;

public interface SimpleCache {

    /**
     * 获取一条数据
     **/
    Object get(String key);

    /**
     * 获取全部数据
     **/
    Map<String, Object> getAll();

    /**
     * 添加一条数据
     **/
    Object put(String key, Object value);

    /**
     * 添加一条数据
     * @param expiration 过期时间
     **/
    Object put(String key, Object value,Long expiration);


    /**
     * 添加一条数据
     * @param expiration 过期时间
     * @param listenter 过期时,在移除过期数据之前调用该监听器
     **/
    Object put(String key, Object value,Long expiration,KeyListenter listenter);

    /**
     * 检查某个key是否存在
     **/
    Boolean exist(String key);

    /**
     * 生成key
     **/
    String genKey(String... key);

    /**
     * 生成key
     **/
    String genKey(List<String> keys);

    /**
     * 删除一个key value
     **/
    void remove(String key);


    /***
     * 删除所有数据
     * **/
    void removeAll();


}
