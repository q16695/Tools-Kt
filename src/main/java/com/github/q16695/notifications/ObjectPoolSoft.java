package com.github.q16695.notifications;

import java.lang.ref.SoftReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ObjectPoolSoft<T> extends ObjectPool_<T> {
    ObjectPoolSoft(PoolableObject<T> poolableObject) {
        this(poolableObject, new ConcurrentLinkedQueue<>());
    }

    public ObjectPoolSoft(PoolableObject<T> poolableObject, Queue<SoftReference<T>> queue) {
        super(poolableObject, queue);
    }

    public T take() {
        while(true) {
            SoftReference ref;
            if ((ref = (SoftReference)this.queue.poll()) != null) {
                T obj;
                if ((obj = (T)ref.get()) == null) {
                    continue;
                }

                return obj;
            }

            T take = (T)this.poolableObject.create();
            return take;
        }
    }

    public void put(T object) {
        this.poolableObject.onReturn(object);
        this.queue.offer(new SoftReference(object));
    }
}
