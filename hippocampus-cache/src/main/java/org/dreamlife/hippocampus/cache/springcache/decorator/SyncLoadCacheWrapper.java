package org.dreamlife.hippocampus.cache.springcache.decorator;

import com.google.common.collect.Maps;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 *
 * 对load操作进行同步
 * 可以避免缓存穿透时大量请求操作数据库
 * 下面封装的方法可以确保当缓存失效后一个进程只会有一个请求穿透到数据库里进行查询
 *
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/4/9
 */
public class SyncLoadCacheWrapper extends AbstractCacheWrapper {

    private final Map<Object, FutureTask> opLock  = Maps.newConcurrentMap();

    public SyncLoadCacheWrapper(Cache proxy) {
        super(proxy);
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
                FutureTask previousTask = opLock.putIfAbsent(key, futureTask);
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
                opLock.remove(key);
            }
        }
        return value;
    }
}
