package com.liuapi.incubator.async.threadpool;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Executors;

public class ListeningExecutorServiceDemo {

    public static void main(String[] args) {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<String> future = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + "execute task....");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("execute task ok!");
            return "execute task callback msg";
        });

        System.out.println("future class is "+future.getClass().getName());
        /**
         *
         *
         * 父类 可以从子类中指定
         * 父类.class isAssignableFrom 子类.class
         * 子类对象 instanceof 父类.class
         */
        System.out.println("ListenableFuture class is assignable from "+ListenableFuture.class.isAssignableFrom(future.getClass()));
        System.out.println("ListenableFutureTask class is assignable from "+ListenableFutureTask.class.isAssignableFrom(future.getClass()));

        FutureCallback callback = new FutureCallback() {

            @Override
            public void onSuccess(@Nullable Object result) {
                System.out.println(Thread.currentThread().getName() + "execute callback....");
                System.out.println("callbackResultAdd -> " + result.toString());
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(Thread.currentThread().getName() + "execute callback....");
                System.out.println("callbackCatchException -> " + t.getMessage());
            }
        };
        Futures.addCallback(future, callback, executorService);
        System.out.println(future.getClass().getSimpleName());
        System.out.println("main-thread-end");
    }
}
