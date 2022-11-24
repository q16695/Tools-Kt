package com.github.q16695.notifications;

public interface ActionHandler<T> {
    void handle(T value);
}

