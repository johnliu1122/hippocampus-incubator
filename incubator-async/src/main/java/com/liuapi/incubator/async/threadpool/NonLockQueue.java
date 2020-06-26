package com.liuapi.incubator.async.threadpool;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @date 2020/6/26
 */
public class NonLockQueue<T> extends AbstractQueue<T> implements Queue<T> {
    private static final Unsafe UNSAFE;
    private static final long headOffset;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
            Class k = NonLockQueue.class;
            headOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("head"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private volatile Node head;
    private volatile Node tail;

    public NonLockQueue() {
        Node empty = new Node(null);
        head = tail = empty;
    }

    public boolean casHead(Node cmp, Node val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean offer(T t) {
        Node<T> n = new Node(t);
        for (; ; ) {
            Node<T> p = tail;
            if (!p.casNext(null, n)) {
                continue;
            }
            tail = n;
            break;
        }
        return true;
    }

    @Override
    public T poll() {
        for (; ; ) {
            Node<T> h = head;
            Node<T> next = h.next;
            if (next == null) {
                return null;
            }
            if (!casHead(h, next)) {
                continue;
            }
            h.next = h;
            T val = next.item;
            next.item = null;
            return val;
        }
    }

    @Override
    public T peek() {
        Node<T> p = this.head;
        return p!=null?p.item:null;
    }

    private static class Node<T> {
        private static final Unsafe UNSAFE;
        private static final long nextOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (Unsafe) f.get(null);
                Class<?> k = Node.class;
                nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        private volatile Node next;
        private volatile T item;

        public Node(T e) {
            item = e;
        }

        public boolean casNext(Node cmp, Node val) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }
    }
}
