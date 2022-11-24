package com.github.q16695.notifications;

import java.lang.ref.SoftReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class NonBlockingSoftPool<T> extends ObjectPool<T> {
    private final Queue<SoftReference<T>> queue;
    private final PoolableObject<T> poolableObject;

    NonBlockingSoftPool(PoolableObject<T> poolableObject) {
        this(poolableObject, new ConcurrentLinkedQueue<>());
    }

    NonBlockingSoftPool(PoolableObject<T> poolableObject, Queue<SoftReference<T>> queue) {
        this.poolableObject = poolableObject;
        this.queue = queue;
    }

    public T take() {
        while(true) {
            SoftReference ref;
            if ((ref = (SoftReference)this.queue.poll()) != null) {
                T obj;
                if ((obj = (T)ref.get()) == null) {
                    continue;
                }

                this.poolableObject.onTake(obj);
                return obj;
            }

            T take = this.poolableObject.create();
            this.poolableObject.onTake(take);
            return take;
        }
    }

    public T takeInterruptibly() throws InterruptedException {
        return this.take();
    }

    public void put(T object) {
        this.poolableObject.onReturn(object);
        this.queue.offer(new SoftReference<>(object));
    }

    public T newInstance() {
        return this.poolableObject.create();
    }
}
