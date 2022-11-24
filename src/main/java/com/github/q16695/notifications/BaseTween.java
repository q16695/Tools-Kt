package com.github.q16695.notifications;

public abstract class BaseTween<T> {
    protected static final int INVALID = 0;
    protected static final int START = 1;
    protected static final int RUN = 2;
    protected static final int FINISHED = 3;
    protected final TweenEngine animator;
    protected int state = 0;
    protected int repeatCountOrig;
    private int repeatCount;
    protected boolean canAutoReverse;
    private boolean isPaused;
    protected boolean isCanceled;
    protected boolean isInitialized;
    private float startDelay;
    private float repeatDelay;
    protected float duration;
    protected float currentTime;
    private static final boolean START_VALUES = true;
    private static final boolean TARGET_VALUES = false;
    private static final boolean FORWARDS = true;
    private static final boolean REVERSE = false;
    private boolean direction = true;
    private boolean canTriggerBeginEvent;
    protected boolean isInAutoReverse;
    private Object userData;
    protected boolean isAutoRemoveEnabled;
    protected boolean isAutoStartEnabled;
    private UpdateAction startEventCallback;
    private UpdateAction endEventCallback;
    private static final TweenCallback[] TEMP_EMPTY = new TweenCallback[0];
    private TweenCallback[] forwards_Begin;
    private TweenCallback[] forwards_Start;
    private TweenCallback[] forwards_End;
    private TweenCallback[] forwards_Complete;
    private TweenCallback[] reverse_Begin;
    private TweenCallback[] reverse_Start;
    private TweenCallback[] reverse_End;
    private TweenCallback[] reverse_Complete;

    public BaseTween(TweenEngine animator) {
        this.startEventCallback = TweenEngine.NULL_ACTION;
        this.endEventCallback = TweenEngine.NULL_ACTION;
        this.forwards_Begin = new TweenCallback[0];
        this.forwards_Start = new TweenCallback[0];
        this.forwards_End = new TweenCallback[0];
        this.forwards_Complete = new TweenCallback[0];
        this.reverse_Begin = new TweenCallback[0];
        this.reverse_Start = new TweenCallback[0];
        this.reverse_End = new TweenCallback[0];
        this.reverse_Complete = new TweenCallback[0];
        this.animator = animator;
    }

    protected void reset() {
        this.state = 1;
        this.direction = true;
        this.canTriggerBeginEvent = true;
        this.currentTime = -this.startDelay;
        this.isInAutoReverse = false;
        this.repeatCount = this.repeatCountOrig;
    }

    protected void destroy() {
        this.repeatCount = this.repeatCountOrig = 0;
        this.state = 0;
        this.duration = this.startDelay = this.repeatDelay = this.currentTime = 0.0F;
        this.isPaused = this.isCanceled = this.isInAutoReverse = this.isInitialized = false;
        this.canTriggerBeginEvent = true;
        this.clearCallbacks_();
        this.userData = null;
        this.endEventCallback = this.startEventCallback = TweenEngine.NULL_ACTION;
        this.isAutoRemoveEnabled = this.isAutoStartEnabled = true;
    }

    public T clearCallbacks() {
        synchronized(TEMP_EMPTY) {
            this.clearCallbacks_();
            return (T)this;
        }
    }

    private void clearCallbacks_() {
        this.forwards_Begin = new TweenCallback[0];
        this.forwards_Start = new TweenCallback[0];
        this.forwards_End = new TweenCallback[0];
        this.forwards_Complete = new TweenCallback[0];
        this.reverse_Begin = new TweenCallback[0];
        this.reverse_Start = new TweenCallback[0];
        this.reverse_End = new TweenCallback[0];
        this.reverse_Complete = new TweenCallback[0];
    }

    public final T addCallback(TweenCallback callback) {
        int triggers = callback.triggers;
        synchronized(TEMP_EMPTY) {
            int currentLength;
            int newLength;
            TweenCallback[] copy;
            if ((triggers & 1) == 1) {
                currentLength = this.forwards_Begin.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.forwards_Begin, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.forwards_Begin = copy;
            }

            if ((triggers & 2) == 2) {
                currentLength = this.forwards_Start.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.forwards_Start, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.forwards_Start = copy;
            }

            if ((triggers & 4) == 4) {
                currentLength = this.forwards_End.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.forwards_End, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.forwards_End = copy;
            }

            if ((triggers & 8) == 8) {
                currentLength = this.forwards_Complete.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.forwards_Complete, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.forwards_Complete = copy;
            }

            if ((triggers & 16) == 16) {
                currentLength = this.reverse_Begin.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.reverse_Begin, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.reverse_Begin = copy;
            }

            if ((triggers & 32) == 32) {
                currentLength = this.reverse_Start.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.reverse_Start, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.reverse_Start = copy;
            }

            if ((triggers & 64) == 64) {
                currentLength = this.reverse_End.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.reverse_End, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.reverse_End = copy;
            }

            if ((triggers & 128) == 128) {
                currentLength = this.reverse_Complete.length;
                newLength = currentLength + 1;
                copy = new TweenCallback[newLength];
                System.arraycopy(this.reverse_Complete, 0, copy, 0, Math.min(currentLength, newLength));
                copy[currentLength] = callback;
                this.reverse_Complete = copy;
            }

            return (T)this;
        }
    }

    public T delay(float delay) {
        this.animator.flushRead();
        this.delay__(delay);
        this.animator.flushWrite();
        return (T)this;
    }

    protected T delay__(float delay) {
        this.startDelay += delay;
        this.currentTime -= delay;
        return (T)this;
    }

    public void cancel() {
        this.isCanceled = true;
        this.animator.flushWrite();
    }

    public void free() {
    }

    public void pause() {
        this.isPaused = true;
        this.animator.flushWrite();
    }

    public void resume() {
        this.isPaused = false;
        this.animator.flushWrite();
    }

    public T repeat(int count, float delay) {
        this.repeat__(count, delay);
        this.animator.flushWrite();
        return (T)this;
    }

    private T repeat__(int count, float delay) {
        if (count < -1) {
            throw new RuntimeException("Count " + count + " is an invalid option. It must be -1 (Tween.INFINITY) for infinite or > 0 for finite.");
        } else {
            this.repeatCountOrig = count;
            this.repeatCount = count;
            this.repeatDelay = delay;
            this.canAutoReverse = false;
            return (T)this;
        }
    }

    public T repeatAutoReverse(int count, float delay) {
        this.repeat__(count, delay);
        this.canAutoReverse = true;
        this.animator.flushWrite();
        return (T)this;
    }

    public final T setStartCallback(UpdateAction<T> startCallback) {
        if (startCallback == null) {
            this.startEventCallback = TweenEngine.NULL_ACTION;
        } else {
            this.startEventCallback = startCallback;
        }

        this.animator.flushWrite();
        return (T)this;
    }

    public final T setEndCallback(UpdateAction<T> endCallback) {
        if (endCallback == null) {
            this.endEventCallback = TweenEngine.NULL_ACTION;
        } else {
            this.endEventCallback = endCallback;
        }

        this.animator.flushWrite();
        return (T)this;
    }

    public T startUnmanaged() {
        this.startUnmanaged__();
        this.animator.flushWrite();
        return (T)this;
    }

    void startUnmanaged__() {
        this.setup__();
    }

    public T start() {
        this.animator.flushRead();
        this.animator.add__(this);
        this.animator.flushWrite();
        return (T)this;
    }

    protected void setup__() {
        this.canTriggerBeginEvent = true;
        this.state = 1;
    }

    public final float getCurrentTime() {
        this.animator.flushRead();
        return this.currentTime;
    }

    public final float getStartDelay() {
        this.animator.flushRead();
        return this.startDelay;
    }

    public float getDuration() {
        this.animator.flushRead();
        return this.duration;
    }

    public float getFullDuration() {
        this.animator.flushRead();
        return this.getFullDuration__();
    }

    final float getFullDuration__() {
        return this.repeatCountOrig < 0 ? -1.0F : this.startDelay + this.duration + (this.repeatDelay + this.duration) * (float)this.repeatCountOrig;
    }

    public final int getRepeatCount() {
        this.animator.flushRead();
        return this.repeatCountOrig;
    }

    public final float getRepeatDelay() {
        this.animator.flushRead();
        return this.repeatDelay;
    }

    public final boolean getDirection() {
        this.animator.flushRead();
        return this.direction;
    }

    public final boolean isInDelay() {
        this.animator.flushRead();
        return this.state == 1;
    }

    public final boolean isInAutoReverse() {
        this.animator.flushRead();
        return this.isInAutoReverse;
    }

    public boolean isInitialized() {
        this.animator.flushRead();
        return this.isInitialized;
    }

    public boolean isFinished() {
        this.animator.flushRead();
        return this.state == 3 || this.isCanceled;
    }

    public boolean canAutoReverse() {
        this.animator.flushRead();
        return this.canAutoReverse;
    }

    public boolean isPaused() {
        this.animator.flushRead();
        return this.isPaused;
    }

    public void disableAutoRemove() {
        this.isAutoRemoveEnabled = false;
        this.animator.flushWrite();
    }

    public void disableAutoStart() {
        this.isAutoStartEnabled = false;
        this.animator.flushWrite();
    }

    public T setUserData(Object data) {
        this.userData = data;
        this.animator.flushWrite();
        return (T)this;
    }

    public T getUserData() {
        this.animator.flushRead();
        return (T)this.userData;
    }

    protected abstract boolean containsTarget(Object var1);

    protected abstract boolean containsTarget(Object var1, int var2);

    protected abstract void update(boolean var1, float var2);

    protected abstract void setValues(boolean var1, boolean var2);

    public T setProgress(float percentage) {
        this.animator.flushRead();
        return this.setProgress(percentage, this.direction);
    }

    public T setProgress(float percentage, boolean direction) {
        if (!(percentage < -0.0F) && !(percentage > 1.0F)) {
            synchronized(TEMP_EMPTY) {
                this.reset();
                float duration = this.duration;
                float percentageValue = duration * percentage;
                boolean goesReverse = !direction && this.canAutoReverse;
                float adjustmentTime;
                if (goesReverse) {
                    float timeSpentToGetToEnd = duration + this.startDelay;
                    float timeSpentInReverseFromEnd = duration - percentageValue;
                    adjustmentTime = timeSpentToGetToEnd + timeSpentInReverseFromEnd;
                } else {
                    adjustmentTime = percentageValue + this.startDelay;
                }

                TweenCallback[] forwards_Begin_saved = this.forwards_Begin;
                TweenCallback[] forwards_Start_saved = this.forwards_Start;
                TweenCallback[] forwards_End_saved = this.forwards_End;
                TweenCallback[] forwards_Complete_saved = this.forwards_Complete;
                TweenCallback[] reverse_Begin_saved = this.reverse_Begin;
                TweenCallback[] reverse_Start_saved = this.reverse_Start;
                TweenCallback[] reverse_End_saved = this.reverse_End;
                TweenCallback[] reverse_Complete_saved = this.reverse_Complete;
                this.forwards_Begin = TEMP_EMPTY;
                this.forwards_Start = TEMP_EMPTY;
                this.forwards_End = TEMP_EMPTY;
                this.forwards_Complete = TEMP_EMPTY;
                this.reverse_Begin = TEMP_EMPTY;
                this.reverse_Start = TEMP_EMPTY;
                this.reverse_End = TEMP_EMPTY;
                this.reverse_Complete = TEMP_EMPTY;
                this.update__(adjustmentTime);
                this.forwards_Begin = forwards_Begin_saved;
                this.forwards_Start = forwards_Start_saved;
                this.forwards_End = forwards_End_saved;
                this.forwards_Complete = forwards_Complete_saved;
                this.reverse_Begin = reverse_Begin_saved;
                this.reverse_Start = reverse_Start_saved;
                this.reverse_End = reverse_End_saved;
                this.reverse_Complete = reverse_Complete_saved;
                return (T)this;
            }
        } else {
            throw new RuntimeException("Cannot set the progress <0 or >1");
        }
    }

    protected void initializeValues() {
    }

    protected boolean cancelTarget(Object target) {
        if (this.containsTarget(target)) {
            this.cancel();
            return true;
        } else {
            return false;
        }
    }

    protected boolean cancelTarget(Object target, int tweenType) {
        if (this.containsTarget(target, tweenType)) {
            this.cancel();
            return true;
        } else {
            return false;
        }
    }

    protected void adjustForRepeat_AutoReverse(boolean newDirection) {
        this.state = 1;
        if (newDirection) {
            this.currentTime = 0.0F;
        } else {
            this.currentTime = this.duration;
        }

    }

    protected void adjustForRepeat_Linear(boolean newDirection) {
        this.state = 1;
        if (newDirection) {
            this.currentTime = 0.0F;
        } else {
            this.currentTime = this.duration;
        }

    }

    public final float update(float delta) {
        this.animator.flushRead();
        float v = this.update__(delta);
        this.animator.flushWrite();
        return v;
    }

    protected final float update__(float delta) {
        if (!this.isPaused && !this.isCanceled) {
            if (this.isInAutoReverse) {
                delta = -delta;
            }

            boolean direction = delta >= 0.0F;
            this.direction = direction;
            float duration = this.duration;
            this.startEventCallback.onEvent(this);

            while(true) {
                float newTime = this.currentTime + delta;
                TweenCallback[] callbacks;
                int i;
                int n;
                int repeatCountStack;
                TweenCallback[] callbacks2;
                if (direction) {
                    switch(this.state) {
                        case 1:
                            if (newTime <= 0.0F) {
                                this.currentTime = newTime;
                                this.endEventCallback.onEvent(this);
                                return 0.0F;
                            }

                            this.currentTime = 0.0F;
                            if (this.canTriggerBeginEvent) {
                                this.canTriggerBeginEvent = false;
                                if (!this.isInitialized) {
                                    this.isInitialized = true;
                                    this.initializeValues();
                                }

                                callbacks = this.forwards_Begin;
                                i = 0;

                                for(i = callbacks.length; i < i; ++i) {
                                    callbacks[i].onEvent(1, this);
                                }
                            }

                            callbacks = this.forwards_Start;
                            i = 0;

                            for(i = callbacks.length; i < i; ++i) {
                                callbacks[i].onEvent(2, this);
                            }

                            this.state = 2;
                            this.setValues(false, true);
                            delta = newTime;
                        case 2:
                            if (newTime <= duration) {
                                this.currentTime = newTime;
                                this.update(true, delta);
                                this.endEventCallback.onEvent(this);
                                return 0.0F;
                            }

                            this.state = 3;
                            this.currentTime = duration;
                            repeatCountStack = this.repeatCount;
                            if (repeatCountStack == 0) {
                                this.setValues(false, false);
                                callbacks = this.forwards_End;
                                i = 0;

                                for(i = callbacks.length; i < i; ++i) {
                                    callbacks[i].onEvent(4, this);
                                }

                                callbacks2 = this.forwards_Complete;
                                i = 0;

                                for(n = callbacks2.length; i < n; ++i) {
                                    callbacks2[i].onEvent(8, this);
                                }

                                this.canTriggerBeginEvent = true;
                                this.isInAutoReverse = false;
                                this.repeatCount = this.repeatCountOrig;
                                this.endEventCallback.onEvent(this);
                                return newTime - duration;
                            }

                            this.update(true, delta);
                            if (repeatCountStack > 0) {
                                --this.repeatCount;
                            }

                            callbacks = this.forwards_End;
                            i = 0;

                            for(i = callbacks.length; i < i; ++i) {
                                callbacks[i].onEvent(4, this);
                            }

                            if (!this.canAutoReverse) {
                                this.isInAutoReverse = false;
                                this.adjustForRepeat_Linear(true);
                                delta = newTime - duration;
                                this.currentTime = -this.repeatDelay + delta;
                                break;
                            }

                            callbacks2 = this.forwards_Complete;
                            i = 0;

                            for(n = callbacks2.length; i < n; ++i) {
                                callbacks2[i].onEvent(8, this);
                            }

                            this.canTriggerBeginEvent = true;
                            this.isInAutoReverse = !this.isInAutoReverse;
                            direction = false;
                            this.adjustForRepeat_AutoReverse(false);
                            this.currentTime += this.repeatDelay;
                            delta = -newTime + duration;
                            break;
                        case 3:
                            if (!(newTime <= 0.0F) && !(newTime > duration)) {
                                this.state = 1;
                                this.update(true, delta);
                                break;
                            }

                            this.currentTime = newTime;
                            this.endEventCallback.onEvent(this);
                            return 0.0F;
                        default:
                            throw new RuntimeException("Unexpected state!! '" + this.state + "'");
                    }
                } else {
                    switch(this.state) {
                        case 1:
                            if (newTime >= duration) {
                                this.currentTime = newTime;
                                this.endEventCallback.onEvent(this);
                                return 0.0F;
                            }

                            this.currentTime = duration;
                            if (this.canTriggerBeginEvent) {
                                this.canTriggerBeginEvent = false;
                                if (!this.isInitialized) {
                                    this.isInitialized = true;
                                    this.initializeValues();
                                }

                                callbacks = this.reverse_Begin;
                                i = 0;

                                for(i = callbacks.length; i < i; ++i) {
                                    callbacks[i].onEvent(16, this);
                                }
                            }

                            callbacks = this.reverse_Start;
                            i = 0;

                            for(i = callbacks.length; i < i; ++i) {
                                callbacks[i].onEvent(32, this);
                            }

                            this.state = 2;
                            this.setValues(true, false);
                            delta = -(duration - newTime);
                        case 2:
                            if (newTime >= 0.0F) {
                                this.currentTime = newTime;
                                this.update(false, delta);
                                this.endEventCallback.onEvent(this);
                                return 0.0F;
                            }

                            this.state = 3;
                            this.currentTime = 0.0F;
                            repeatCountStack = this.repeatCount;
                            if (repeatCountStack == 0) {
                                if (duration <= 1.0E-6F) {
                                    this.setValues(true, false);
                                } else {
                                    this.setValues(true, true);
                                }

                                callbacks = this.reverse_End;
                                i = 0;

                                for(i = callbacks.length; i < i; ++i) {
                                    callbacks[i].onEvent(64, this);
                                }

                                callbacks2 = this.reverse_Complete;
                                i = 0;

                                for(n = callbacks2.length; i < n; ++i) {
                                    callbacks2[i].onEvent(128, this);
                                }

                                this.canTriggerBeginEvent = true;
                                this.isInAutoReverse = false;
                                this.repeatCount = this.repeatCountOrig;
                                this.endEventCallback.onEvent(this);
                                return newTime;
                            }

                            this.update(false, delta);
                            if (repeatCountStack > 0) {
                                --this.repeatCount;
                            }

                            callbacks = this.reverse_End;
                            i = 0;

                            for(i = callbacks.length; i < i; ++i) {
                                callbacks[i].onEvent(64, this);
                            }

                            if (!this.canAutoReverse) {
                                this.isInAutoReverse = false;
                                this.adjustForRepeat_Linear(false);
                                this.currentTime = this.repeatDelay + newTime;
                                break;
                            }

                            callbacks2 = this.reverse_Complete;
                            i = 0;

                            for(n = callbacks2.length; i < n; ++i) {
                                callbacks2[i].onEvent(128, this);
                            }

                            this.canTriggerBeginEvent = true;
                            this.isInAutoReverse = !this.isInAutoReverse;
                            direction = true;
                            this.adjustForRepeat_AutoReverse(true);
                            this.currentTime -= this.repeatDelay;
                            delta = -newTime;
                            break;
                        case 3:
                            if (!(newTime < 0.0F) && !(newTime >= duration)) {
                                this.state = 1;
                                this.update(false, delta);
                                break;
                            }

                            this.currentTime = newTime;
                            this.endEventCallback.onEvent(this);
                            return 0.0F;
                        default:
                            throw new RuntimeException("Unexpected state!! '" + this.state + "'");
                    }
                }
            }
        } else {
            return delta;
        }
    }
}
