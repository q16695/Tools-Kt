package com.github.q16695.notifications;

public interface INotify {
    void close();

    void shake(int durationInMillis, int amplitude);

    void setVisible(boolean b);

    void onClick(int x, int y);
}

