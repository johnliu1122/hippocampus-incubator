package com.liuapi.incubator.async.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    @Test
    public void s() throws InterruptedException {
        Semaphore mutex = new Semaphore(-9);

        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            service.submit(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("subTask run。。。");
                mutex.release();
            });
        }
        System.out.println(Thread.currentThread().getName()+" 等待获取锁");
        // 获取锁
        mutex.acquire();
        System.out.println(Thread.currentThread().getName()+" 获取锁成功");
        service.shutdown();
    }
}
