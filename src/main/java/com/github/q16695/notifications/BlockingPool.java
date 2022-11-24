package com.github.q16695.notifications;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BlockingPool<T> extends ObjectPool<T> {
    private final BlockingQueue<T> queue;
    private final PoolableObject<T> poolableObject;

    BlockingPool(PoolableObject<T> poolableObject, int size) {
        this(poolableObject, new ArrayBlockingQueue(size), size);
    }

    BlockingPool(PoolableObject<T> poolableObject, BlockingQueue<T> queue, int size) {
        this.poolableObject = poolableObject;
        this.queue = queue;

        for(int x = 0; x < size; ++x) {
            T e = poolableObject.create();
            poolableObject.onReturn(e);
            this.queue.add(e);
        }

    }

    public T take() {
        try {
            return this.takeInterruptibly();
        } catch (InterruptedException var2) {
            return null;
        }
    }

    public T takeInterruptibly() throws InterruptedException {
        T take = this.queue.take();
        this.poolableObject.onTake(take);
        return take;
    }

    public void put(T object) {
        this.poolableObject.onReturn(object);
        this.queue.offer(object);
    }

    public T newInstance() {
        return this.poolableObject.create();
    }
}

