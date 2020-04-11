package org.dreamlife.hippocampus.cache.springcache.decorator;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/12
 */
public abstract class AbstractCacheWrapper implements Cache {

    protected final Cache proxy;

    public AbstractCacheWrapper(Cache proxy){
        this.proxy=proxy;
    }

    @Override
    public String getName() {
        return proxy.getName();
    }

    @Override
    public Object getNativeCache() {
        return proxy.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object o) {
        return proxy.get(o);
    }

    @Override
    public <T> T get(Object o, Class<T> aClass) {
        return proxy.get(o,aClass);
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return proxy.get(o,callable);
    }

    @Override
    public void put(Object o, Object o1) {
        proxy.put(o,o1);
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return proxy.putIfAbsent(o,o1);
    }

    @Override
    public void evict(Object o) {
        proxy.evict(o);
    }

    @Override
    public void clear() {
        proxy.clear();
    }
}
