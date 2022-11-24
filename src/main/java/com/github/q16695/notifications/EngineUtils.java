package com.github.q16695.notifications;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

class EngineUtils {
    EngineUtils() {
    }

    static long nanoTime() {
        return System.nanoTime();
    }

    static <T> ObjectPool<T> getPool(boolean threadSafe, PoolableObject<T> poolableObject) {
        return threadSafe ? new ObjectPoolSoft(poolableObject, new ConcurrentLinkedQueue()) : new ObjectPoolSoft(poolableObject, new ArrayDeque());
    }
}


