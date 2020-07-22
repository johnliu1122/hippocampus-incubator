package com.liuapi.incubator.async.threadpool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Foo {
    private Lock lock = new ReentrantLock();
    private Condition ready = lock.newCondition();
    private int order = 1;


    public Foo() {
        
    }

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        try{
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            order++;
            ready.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        try{
            while(order!=2){
                ready.await();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            order++;
            ready.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        try{
            while(order!=3){
                ready.await();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
            ready.signalAll();
        }finally {
            lock.unlock();
        }
    }
}