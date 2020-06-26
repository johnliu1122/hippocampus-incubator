package com.liuapi.incubator.async.threadpool;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/6/26
 */
public class NonLockQueue<T> {
    private volatile Node head;
    private volatile Node tail;
    private static final Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;

    private AtomicInteger size = new AtomicInteger(0);
    static {
        try{
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
            Class k = NonLockQueue.class;
            headOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("tail"));
        }catch (Exception e){
            throw new Error(e);
        }
    }
    public boolean casHead(Node cmp,Node val){
        return UNSAFE.compareAndSwapObject(this,headOffset,cmp,val);
    }

    public boolean casTail(Node cmp,Node val){
        return UNSAFE.compareAndSwapObject(this,tailOffset,cmp,val);
    }

    public NonLockQueue(){
        Node empty = new Node(null);
        head = tail = empty;
    }
    static class Node<T>{
        private static final Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;
        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (Unsafe) f.get(null);
                Class<?> k = Node.class;
                itemOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        Node next;
        T item;
        public Node(T e){
            item = e;
        }
        public boolean casNext(Node cmp,Node val){
            return UNSAFE.compareAndSwapObject(this, nextOffset,cmp, val);
        }
    }

    public void enqueue(T e){
        Node<T> n = new Node(e);
        for(;;){
            Node<T>  t = tail;
            if(!t.casNext(null,n)){ // fail to link in then continue
                continue;
            }
            tail = n;
            int s = size.incrementAndGet();
            System.out.println("enqueue value "+e+"|size "+s);
            break;
        }

    }

    public T dequeue(){
        // assert there have at least two elements
        for(;;){
            if(size.get()<1){
                return null;
            }
            Node<T> h = head;
            Node<T> next = h.next;

            if(!casHead(h,next)){
                continue;
            }
            h.next = null;
            int s =  size.decrementAndGet();
            T val = next.item;
            next.item = null;
            System.out.println("dequeue value "+val+"|size "+s);
            return val;
        }
    }
}
