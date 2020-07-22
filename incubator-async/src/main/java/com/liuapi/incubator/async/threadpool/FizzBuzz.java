package com.liuapi.incubator.async.threadpool;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

class FizzBuzz {
    private int n;
    private Semaphore numberSemp = new Semaphore(1);
    private Semaphore fuzzSemp = new Semaphore(0);
    private Semaphore buzzSemp = new Semaphore(0);
    private Semaphore fuzzbuzzSemp = new Semaphore(0);

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if( i%3==0&&i%5!=0){
                fuzzSemp.acquire();
                printFizz.run();
                numberSemp.release();
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if( i%5==0&&i%3!=0){
                buzzSemp.acquire();
                printBuzz.run();
                numberSemp.release();
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if( i%3==0&&i%5==0){
                fuzzbuzzSemp.acquire();
                printFizzBuzz.run();
                numberSemp.release();
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            numberSemp.acquire();
            if( i%3!=0&&i%5!=0){
                printNumber.accept(i);
                numberSemp.release();
            }else if(i%3==0&&i%5==0){
                fuzzbuzzSemp.release();
            }else if(i%3==0){
                fuzzSemp.release();
            }else{
                buzzSemp.release();
            }
        }
    }
}