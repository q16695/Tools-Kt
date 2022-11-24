package com.github.q16695.notifications;

public abstract class TweenCallback {
    int triggers;

    public TweenCallback() {
        this.triggers = 8;
    }

    public TweenCallback(int triggers) {
        this.triggers = triggers;
    }

    public abstract void onEvent(int var1, BaseTween<?> var2);

    public static final class Events {
        public static final int BEGIN = 1;
        public static final int START = 2;
        public static final int END = 4;
        public static final int COMPLETE = 8;
        public static final int BACK_BEGIN = 16;
        public static final int BACK_START = 32;
        public static final int BACK_END = 64;
        public static final int BACK_COMPLETE = 128;
        public static final int ANY_FORWARD = 15;
        public static final int ANY_BACKWARD = 240;
        public static final int ANY = 255;

        private Events() {
        }
    }
}

