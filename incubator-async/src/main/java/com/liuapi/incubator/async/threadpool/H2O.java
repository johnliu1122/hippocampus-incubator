package com.liuapi.incubator.async.threadpool;

import java.util.concurrent.Semaphore;

class H2O {
    private Semaphore hSemaphore = new Semaphore(2);
    private Semaphore oSemaphore = new Semaphore(0);
    public H2O() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hSemaphore.acquire();
        releaseHydrogen.run();
        oSemaphore.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oSemaphore.acquire(2);
        releaseOxygen.run();
        hSemaphore.release(2);
    }
}