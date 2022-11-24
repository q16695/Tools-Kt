package com.github.q16695.notifications;

public abstract class TweenEquation {
    public TweenEquation() {
    }

    public abstract float compute(float var1);

    public boolean isValueOf(String string) {
        return string.equals(this.toString());
    }
}

