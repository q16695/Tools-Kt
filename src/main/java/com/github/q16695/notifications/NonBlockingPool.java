package com.github.q16695.notifications;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class NonBlockingPool<T> extends ObjectPool<T> {
    private final Queue<T> queue;
    private final PoolableObject<T> poolableObject;

    NonBlockingPool(PoolableObject<T> poolableObject) {
        this(poolableObject, new ConcurrentLinkedQueue<>());
    }

    NonBlockingPool(PoolableObject<T> poolableObject, Queue<T> queue) {
        this.poolableObject = poolableObject;
        this.queue = queue;
    }

    public T take() {
        T take = this.queue.poll();
        if (take == null) {
            take = this.poolableObject.create();
        }

        this.poolableObject.onTake(take);
        return take;
    }

    public T takeInterruptibly() throws InterruptedException {
        return this.take();
    }

    public void put(T object) {
        this.poolableObject.onReturn(object);
        this.queue.offer(object);
    }

    public T newInstance() {
        return this.poolableObject.create();
    }
}

