package com.liuapi.incubator.async.threadpool;

import com.google.common.collect.Queues;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class NonLockQueueTest {
    @org.junit.jupiter.api.Test
    void main() {
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