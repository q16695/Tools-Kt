package com.github.q16695.notifications;

import java.lang.ref.SoftReference;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public abstract class ObjectPool<T> implements Pool<T> {
    public ObjectPool() {
    }

    public static String getVersion() {
        return "2.10";
    }

    public static <T> ObjectPool<T> Blocking(PoolableObject<T> poolableObject, int size) {
        return new BlockingPool<>(poolableObject, size);
    }

    public static <T> ObjectPool<T> Blocking(PoolableObject<T> poolableObject, BlockingQueue<T> queue, int size) {
        return new BlockingPool<>(poolableObject, queue, size);
    }

    public static <T> ObjectPool<T> NonBlocking(PoolableObject<T> poolableObject) {
        return new NonBlockingPool<>(poolableObject);
    }

    public static <T> ObjectPool<T> NonBlocking(PoolableObject<T> poolableObject, Queue<T> queue) {
        return new NonBlockingPool<>(poolableObject, queue);
    }

    public static <T> ObjectPool<T> NonBlockingSoftReference(PoolableObject<T> poolableObject) {
        return new NonBlockingSoftPool<>(poolableObject);
    }

    public static <T> ObjectPool<T> NonBlockingSoftReference(PoolableObject<T> poolableObject, Queue<SoftReference<T>> queue) {
        return new NonBlockingSoftPool<>(poolableObject, queue);
    }
}

