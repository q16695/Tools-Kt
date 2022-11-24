package com.github.q16695.notifications;

import java.util.Queue;

public class ObjectPool_<T> extends ObjectPool<T> {
    protected final Queue queue;
    protected final PoolableObject poolableObject;

    public ObjectPool_(PoolableObject poolableObject, Queue queue) {
        this.poolableObject = poolableObject;
        this.queue = queue;
    }

    public T take() {
        T take = (T)this.queue.poll();
        if (take == null) {
            take = (T)this.poolableObject.create();
        }

        return take;
    }

    @Override
    public T takeInterruptibly() throws InterruptedException {
        return null;
    }

    public void put(T object) {
        this.poolableObject.onReturn(object);
        this.queue.offer(object);
    }

    @Override
    public T newInstance() {
        return null;
    }
}

