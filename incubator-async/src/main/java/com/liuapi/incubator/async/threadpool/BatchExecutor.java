package com.liuapi.incubator.async.threadpool;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.*;

/**
 * 批量执行任务
 *
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/6/23
 */
public class BatchExecutor implements Executor {
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(1024);

    private int COMSUMER_THREAD_COUNT = 5;
    //启动5个消费者线程
    // 执行批量任务
    private Executor executor = Executors.newFixedThreadPool(COMSUMER_THREAD_COUNT);

    @Override
    public void execute(Runnable task) {
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        for (int i = 0; i < COMSUMER_THREAD_COUNT; i++) {
            executor.execute(() -> {
                try {
                    while (true) {
                        List<Runnable> tasks = Lists.newArrayList();
                        Runnable task = queue.take();
                        while (task != null) {
                            tasks.add(task);
                            task = queue.poll();
                        }
                        execute(tasks);
                    }
                } catch (InterruptedException e) {
                    // 就这样优雅退出
                }
            });
        }
    }

    public void execute(List<Runnable> tasks) {
        // 执行批量任务
    }
}
