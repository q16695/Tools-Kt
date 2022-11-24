package com.github.q16695.notifications;

public abstract class PoolableObject<T> {
    public PoolableObject() {
    }

    public void onReturn(T object) {
    }

    public void onTake(T object) {
    }

    public abstract T create();
}

