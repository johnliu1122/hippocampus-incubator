package com.liuapi.incubator.async.threadpool;

import com.google.common.collect.Queues;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/6/26
 */
public class QueueDemo {
    public static void main(String[] args) throws InterruptedException {
        NonLockQueue<String > queue = new NonLockQueue<>();

        ExecutorService executorService = new ThreadPoolExecutor(
                10,10,0, TimeUnit.MILLISECONDS, Queues.newLinkedBlockingQueue()
        );
        Runnable produce = () -> {
            double random = Math.random();
            queue.enqueue("----"+random+"----");
        };

        Runnable consumer = () -> {
            queue.dequeue();
        };
        for(int i =0;i<20;i++){
            executorService.submit(produce);
            executorService.submit(produce);
            executorService.submit(consumer);
        }
        for(int i =0;i<20;i++){
            executorService.submit(consumer);
        }
        executorService.shutdown();
    }
}
