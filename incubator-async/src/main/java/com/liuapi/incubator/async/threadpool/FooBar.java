package com.liuapi.incubator.async.threadpool;

class FooBar {
    private int n;
    private int order = 1;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            
        	// printFoo.run() outputs "foo". Do not change or remove this line.
            synchronized (this){
                while(order!=1){
                    wait();
                }
                printFoo.run();
                order=2;
                notifyAll();

            }

        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            synchronized (this){
                while(order!=2){
                    wait();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                order=1;
                notifyAll();
            }
        }
    }
}