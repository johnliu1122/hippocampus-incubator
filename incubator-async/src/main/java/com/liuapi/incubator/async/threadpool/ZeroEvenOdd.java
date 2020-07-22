package com.liuapi.incubator.async.threadpool;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

class ZeroEvenOdd {
    private int n;
    private Semaphore printZero = new Semaphore(1);
    private Semaphore printEven = new Semaphore(0);
    private Semaphore printOdd = new Semaphore(0);

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for(int i=1;i<=2*n;i++){
            if(i%2==1){
                printZero.acquire();
                printNumber.accept(0);
                if((i+1)%4==0){
                    printEven.release();
                }else{
                    printOdd.release();
                }
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for(int i=1;i<=2*n;i++){
            if(i%2==0&&i%4==0){
                printEven.acquire();
                printNumber.accept(i/2);
                printZero.release();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for(int i=1;i<=2*n;i++){
            if(i%2==0&&i%4==2){
                printOdd.acquire();
                printNumber.accept(i/2);
                printZero.release();
            }
        }
    }
}