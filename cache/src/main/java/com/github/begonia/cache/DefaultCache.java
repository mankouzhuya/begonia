package com.github.begonia.cache;

import com.github.begonia.cache.timing.HashedWheelTimer;
import com.github.begonia.cache.timing.WaitStrategy;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class DefaultCache implements SimpleCache {

    private static LoadingCache<String, Object> cache = Caffeine.newBuilder()
            .maximumSize(1000000)//最多存放1000000个数据
            .expireAfterAccess(1, TimeUnit.DAYS)//缓存1天，7天之后进行回收
            .recordStats()//开启，记录状态数据功能
            .build(s -> null);

    private static HashedWheelTimer timer = new HashedWheelTimer();

    private static DefaultCache instance;

    private DefaultCache(){

    }

    public static SimpleCache getInstance(){
        if(instance == null){
            synchronized (DefaultCache.class){
                if(instance == null){
                    instance = new DefaultCache();
                }
            }
        }
        return instance;
    }

    public static HashedWheelTimer getTimer(){
        return timer;
    }


    /**
     * 获取一条数据
     **/
    @Override
    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * 获取全部数据
     **/
    @Override
    public Map<String, Object> getAll() {
        return cache.asMap();
    }

    /**
     * 添加一条数据
     **/
    @Override
    public Object put(String key, Object value) {
        cache.put(key, value);
        return value;
    }

    @Override
    public Object put(String key, Object value, Long expiration) {
        if(expiration == null || expiration == 0) throw new RuntimeException("过期时间不能为空或者不能为0");
        put(key,value);
        String expKey = "exp_"+key;
        put(expKey, LocalDateTime.now().plusSeconds(expiration));
        timer.schedule(() -> {
            LocalDateTime expTime = (LocalDateTime) get(expKey);
            if(expTime!= null &&  LocalDateTime.now().isBefore(expTime)) return ;
            remove(key);
            remove(expKey); },expiration,TimeUnit.SECONDS);
        return value;
    }

    @Override
    public Object put(String key, Object value, Long expiration, KeyListenter listenter) {
        if(expiration == null || expiration == 0) throw new RuntimeException("过期时间不能为空或者不能为0");
        if(listenter == null) throw new RuntimeException("监听器不能为空");
        put(key,value);
        String expKey = "exp_"+key;
        put(expKey, LocalDateTime.now().plusSeconds(expiration));
        String lisKey = "lis_"+key;
        put(lisKey, listenter);
        timer.schedule(() -> {
            LocalDateTime expTime = (LocalDateTime) get(expKey);
            if(expTime!= null &&  LocalDateTime.now().isBefore(expTime)) return ;
            KeyListenter listenterExc = (KeyListenter) get(lisKey);
            try{listenterExc.onExpire(key,get(key));}catch (Exception e){log.error("key listenter执行错误");}
            remove(key);
            remove(expKey);
            remove(lisKey);
            },expiration,TimeUnit.SECONDS);
        return value;
    }


    /**
     * 检查某个key是否存在
     **/
    @Override
    public Boolean exist(String key) {
        return Objects.isNull(cache.getIfPresent(key));
    }

    /**
     * 生成key
     **/
    @Override
    public String genKey(String... key) {
        if (key == null || key.length < 1) throw new IllegalArgumentException("参数不合法");
        String temp = String.join("&",Arrays.asList(key).stream().sorted((m, n) -> m.compareTo(n)).collect(Collectors.toList()));
        //return DigestUtils.md5DigestAsHex(temp.getBytes(StandardCharsets.UTF_8));
        return temp;
    }

    /**
     * 生成key
     **/
    @Override
    public String genKey(List<String> keys) {
        if (keys == null || keys.size() < 1) throw new IllegalArgumentException("参数不合法");
        String temp = String.join("&",keys.stream().sorted((m, n) -> m.compareTo(n)).collect(Collectors.toList()));
       // return DigestUtils.md5DigestAsHex(temp.getBytes(StandardCharsets.UTF_8));
        return temp;
    }

    /**
     * 删除一个key value
     **/
    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }

    /***
     * 删除所有数据
     * **/
    @Override
    public void removeAll() {
        cache.invalidateAll();
    }

    public static void main(String[] args) throws InterruptedException {
        DefaultCache.getInstance().put("hello","world",5L,(s,t)-> System.out.println("key->"+s+",删除了:+"+t));
        Thread.sleep(6000);
//        System.out.println(DefaultCache.getInstance().get("hello"));
//        Thread.sleep(2000);
//        System.out.println(DefaultCache.getInstance().get("hello"));
        System.out.println(LocalDateTime.now());
        System.out.println("========================");
        DefaultCache.getInstance().put("hello2","world2",5L,(s,t)-> System.out.println("key->"+s+",删除了111111111:+"+t));
        Thread.sleep(2000);
        System.out.println("外面:+"+LocalDateTime.now()+DefaultCache.getInstance().get("hello2"));
        DefaultCache.getInstance().put("hello2","world3",7L,(s,t)-> System.out.println("key->"+s+",删除了222222:+"+t));
        while (true){
            Thread.sleep(1000);
            System.out.println(LocalDateTime.now()+"===>"+DefaultCache.getInstance().get("hello2"));
        }



    }


}
