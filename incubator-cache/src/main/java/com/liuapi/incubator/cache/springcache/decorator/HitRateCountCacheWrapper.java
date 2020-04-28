package com.liuapi.incubator.cache.springcache.decorator;

import lombok.Data;
import org.springframework.cache.Cache;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 * 统计缓存命中率
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/12
 */
public class HitRateCountCacheWrapper extends AbstractCacheWrapper {
    private final CacheIndicator cacheIndicator = new CacheIndicator();

    public HitRateCountCacheWrapper(Cache proxy) {
        super(proxy);
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        cacheIndicator.getRequestCounter().increment();
        return proxy.get(o,
                () -> {
                    /**
                     * 只要调用了Callable，则可以认为是从DB加载数据了，说明缓存没有命中
                     */
                    cacheIndicator.getMissCounter().increment();
                    return callable.call();
                }
        );
    }

    @Data
    public static class CacheIndicator implements Serializable {
        public final LongAdder missCounter = new LongAdder();
        public final LongAdder requestCounter = new LongAdder();

        public double hitRate() {
            long requestCount = requestCounter.longValue();
            long missCount = missCounter.longValue();
            return (requestCount == 0) ? 1.0 : (double) (requestCount-missCount) / requestCount;
        }
    }

}
