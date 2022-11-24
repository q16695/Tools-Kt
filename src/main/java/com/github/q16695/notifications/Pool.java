package com.github.q16695.notifications;


interface Pool<T> {
    T take();

    T takeInterruptibly() throws InterruptedException;

    void put(T var1);

    T newInstance();
}

