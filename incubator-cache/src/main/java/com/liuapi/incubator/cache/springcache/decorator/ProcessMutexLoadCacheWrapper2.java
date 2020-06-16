package com.liuapi.incubator.cache.springcache.decorator;

import com.google.common.collect.Maps;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessMutexLoadCacheWrapper2 extends AbstractCacheWrapper {
    public ProcessMutexLoadCacheWrapper2(Cache proxy) {
        super(proxy);
    }
    private Map<Object, ReentrantLock> opLock = Maps.newConcurrentMap();

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = (T) this.get(key).get();
        if(value != null){
           return value;
        }
        ReentrantLock lock = opLock.computeIfAbsent(key, k -> new ReentrantLock());
        try{
            lock.lock();
            // DCL
            value = (T) this.get(key).get();
            if(value != null){
                return value;
            }
            try {
                value = valueLoader.call();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            this.put(key,value);
            return value;
        }  finally {
            if(!lock.hasQueuedThreads()){
                // 清理map
                opLock.remove(key);
            }
            lock.unlock();
        }
    }
}
