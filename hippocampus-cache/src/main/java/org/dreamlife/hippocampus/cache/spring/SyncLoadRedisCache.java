package org.dreamlife.hippocampus.cache.spring;

import com.google.common.collect.Maps;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 *
 * 对load操作进行同步
 * 可以避免缓存穿透时大量请求操作数据库
 * 下面封装的方法可以确保当缓存失效后只会有一个请求穿透到数据库里进行查询
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/9
 */
public class SyncLoadRedisCache extends RedisCache {
    private final Map<String, FutureTask> opLock  = Maps.newConcurrentMap();

    public SyncLoadRedisCache(String name, byte[] prefix, RedisOperations<?, ?> redisOperations, long expiration) {
        super(name, prefix, redisOperations, expiration);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = (T)this.get(key).get();
        if (value == null) {
            FutureTask<T> futureTask = opLock.get(key);
            if (null == futureTask) {
                futureTask = new FutureTask(
                        () -> {
                            T valueInCache = (T)this.get(key).get();
                            if (valueInCache != null) {
                                return valueInCache;
                            }
                            return valueLoader.call();
                        }
                );
                // t1 t2
                // 赋值成功的futureTask从赋值成功到被移除的这个时间阶段
                FutureTask previousTask = opLock.putIfAbsent((String)key, futureTask);
                if (null == previousTask) {
                    futureTask.run();
                } else {
                    futureTask = previousTask;
                }
            }
            try {
                value = futureTask.get();
                this.putIfAbsent(key, value);
                return value;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                opLock.remove((String)key);
            }
        }
        return value;
    }
}
