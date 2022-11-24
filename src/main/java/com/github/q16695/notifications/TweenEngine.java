package com.github.q16695.notifications;

import java.util.*;

public class TweenEngine {
    private static final BaseTween[] BASE_TWEENS = new BaseTween[0];
    public static final UpdateAction<?> NULL_ACTION = new UpdateAction<Object>() {
        public void onEvent(Object tween) {
        }
    };
    private final Map<Class<?>, TweenAccessor<?>> registeredAccessors = new HashMap();
    private final ObjectPool<Timeline> poolTimeline;
    private final ObjectPool<Tween> poolTween;
    private final int combinedAttrsLimit;
    private final int waypointsLimit;
    private final ArrayList<BaseTween<?>> newTweens = new ArrayList(20);
    private final ArrayList<BaseTween<?>> tweenArrayList = new ArrayList(20);
    private boolean isPaused = false;
    private UpdateAction startEventCallback;
    private UpdateAction endEventCallback;
    private long lastTime;
    private static volatile long lightSyncObject = EngineUtils.nanoTime();

    public static EngineBuilder create() {
        return new EngineBuilder();
    }

    public static TweenEngine build() {
        return new TweenEngine(true, 3, 0, new HashMap());
    }

    public static String getVersion() {
        return "8.2";
    }

    TweenEngine(boolean threadSafe, int combinedAttrsLimit, int waypointsLimit, Map<Class<?>, TweenAccessor<?>> registeredAccessors) {
        this.startEventCallback = NULL_ACTION;
        this.endEventCallback = NULL_ACTION;
        this.lastTime = 0L;
        this.combinedAttrsLimit = combinedAttrsLimit;
        this.waypointsLimit = waypointsLimit;
        this.registeredAccessors.putAll(registeredAccessors);
        PoolableObject<Timeline> timelinePoolableObject = new PoolableObject<Timeline>() {
            public void onReturn(Timeline object) {
                object.destroy();
            }

            public Timeline create() {
                return new Timeline(TweenEngine.this);
            }
        };
        PoolableObject<Tween> tweenPoolableObject = new PoolableObject<Tween>() {
            public void onReturn(Tween object) {
                object.destroy();
            }

            public Tween create() {
                return new Tween(TweenEngine.this, TweenEngine.this.combinedAttrsLimit, TweenEngine.this.waypointsLimit);
            }
        };
        this.poolTimeline = EngineUtils.getPool(threadSafe, timelinePoolableObject);
        this.poolTween = EngineUtils.getPool(threadSafe, tweenPoolableObject);
    }

    long flushRead() {
        return lightSyncObject;
    }

    void flushWrite() {
        lightSyncObject = EngineUtils.nanoTime();
    }

    public TweenAccessor<?> getRegisteredAccessor(Class<?> someClass) {
        this.flushRead();
        return this.registeredAccessors.get(someClass);
    }

    public TweenEngine setStartCallback(UpdateAction<TweenEngine> startCallback) {
        if (startCallback == null) {
            this.startEventCallback = NULL_ACTION;
        } else {
            this.startEventCallback = startCallback;
        }

        this.flushWrite();
        return this;
    }

    public TweenEngine setEndCallback(UpdateAction<TweenEngine> endCallback) {
        if (endCallback == null) {
            this.endEventCallback = NULL_ACTION;
        } else {
            this.endEventCallback = endCallback;
        }

        this.flushWrite();
        return this;
    }

    public TweenEngine add(BaseTween<?> tween) {
        this.flushRead();
        this.add__(tween);
        this.flushWrite();
        return this;
    }

    void add__(BaseTween<?> tween) {
        this.newTweens.add(tween);
        if (tween.isAutoStartEnabled) {
            tween.startUnmanaged__();
        }

    }

    public boolean containsTarget(Object target) {
        this.flushRead();
        Iterator var2 = this.tweenArrayList.iterator();

        BaseTween tween;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            tween = (BaseTween)var2.next();
        } while(!tween.containsTarget(target));

        return true;
    }

    public boolean containsTarget(Object target, int tweenType) {
        this.flushRead();
        Iterator var3 = this.tweenArrayList.iterator();

        BaseTween tween;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            tween = (BaseTween)var3.next();
        } while(!tween.containsTarget(target, tweenType));

        return true;
    }

    public void cancelAll() {
        this.flushRead();
        boolean wasCanceled = false;
        Iterator var2 = this.tweenArrayList.iterator();

        while(var2.hasNext()) {
            BaseTween tween = (BaseTween)var2.next();
            tween.cancel();
        }

        this.flushWrite();
    }

    public boolean cancelTarget(Object target) {
        this.flushRead();
        boolean wasCanceled = false;
        Iterator var3 = this.tweenArrayList.iterator();

        while(var3.hasNext()) {
            BaseTween tween = (BaseTween)var3.next();
            if (tween.cancelTarget(target)) {
                wasCanceled = true;
            }
        }

        this.flushWrite();
        return wasCanceled;
    }

    public boolean cancelTarget(Object target, int tweenType) {
        this.flushRead();
        boolean wasCanceled = false;
        Iterator var4 = this.tweenArrayList.iterator();

        while(var4.hasNext()) {
            BaseTween tween = (BaseTween)var4.next();
            if (tween.cancelTarget(target, tweenType)) {
                wasCanceled = true;
            }
        }

        this.flushWrite();
        return wasCanceled;
    }

    public void ensureCapacity(int minCapacity) {
        this.flushRead();
        this.tweenArrayList.ensureCapacity(minCapacity);
        this.flushWrite();
    }

    public void pause() {
        this.isPaused = true;
        this.flushWrite();
    }

    public void resume() {
        this.isPaused = false;
        this.flushWrite();
    }

    public void resetUpdateTime() {
        this.lastTime = EngineUtils.nanoTime();
        this.flushWrite();
    }

    public void update() {
        this.flushRead();
        long newTime = EngineUtils.nanoTime();
        float deltaTime = (float)(newTime - this.lastTime) / 1.0E9F;
        this.lastTime = newTime;
        this.update__(deltaTime);
        this.flushWrite();
    }

    public void update(long deltaTimeInNanos) {
        this.flushRead();
        float deltaTimeInSec = (float)deltaTimeInNanos / 1.0E9F;
        this.update__(deltaTimeInSec);
        this.flushWrite();
    }

    public final void update(float delta) {
        this.flushRead();
        this.update__(delta);
        this.flushWrite();
    }

    private void update__(float delta) {
        if (!this.isPaused) {
            this.startEventCallback.onEvent(this);
            int size = this.newTweens.size();
            if (size > 0) {
                this.tweenArrayList.addAll(this.newTweens);
                this.newTweens.clear();
            }

            Iterator iterator = this.tweenArrayList.iterator();

            BaseTween tween;
            while(iterator.hasNext()) {
                tween = (BaseTween)iterator.next();
                tween.update__(delta);
            }

            iterator = this.tweenArrayList.iterator();

            while(iterator.hasNext()) {
                tween = (BaseTween)iterator.next();
                if (tween.isAutoRemoveEnabled) {
                    if (tween.state == 3) {
                        tween.setValues(true, false);
                        iterator.remove();
                        tween.free();
                    } else if (tween.isCanceled) {
                        iterator.remove();
                        tween.free();
                    }
                }
            }

            this.endEventCallback.onEvent(this);
        }

    }

    public int size() {
        this.flushRead();
        return this.tweenArrayList.size();
    }

    public int getRunningTweensCount() {
        this.flushRead();
        return getTweensCount(this.tweenArrayList);
    }

    public int getRunningTimelinesCount() {
        this.flushRead();
        return getTimelinesCount(this.tweenArrayList);
    }

    public List<BaseTween<?>> getObjects() {
        this.flushRead();
        return Collections.unmodifiableList(this.tweenArrayList);
    }

    public Timeline createSequential() {
        Timeline timeline = (Timeline)this.poolTimeline.take();
        this.flushRead();
        timeline.setup__(Timeline.Mode.SEQUENTIAL);
        this.flushWrite();
        return timeline;
    }

    public Timeline createParallel() {
        Timeline timeline = (Timeline)this.poolTimeline.take();
        this.flushRead();
        timeline.setup__(Timeline.Mode.PARALLEL);
        this.flushWrite();
        return timeline;
    }

    public Tween to(Object target, int tweenType, float duration) {
        return this.to(target, tweenType, (TweenAccessor)null, duration);
    }

    public <T> Tween to(T target, int tweenType, TweenAccessor<T> targetAccessor, float duration) {
        Tween tween = this.takeTween();
        this.flushRead();
        tween.setup__(target, tweenType, targetAccessor, duration);
        tween.ease__(TweenEquations.Quad_InOut);
        tween.path__(TweenPaths.CatmullRom);
        this.flushWrite();
        return tween;
    }

    public <T> Tween from(T target, int tweenType, float duration) {
        return this.from(target, tweenType, (TweenAccessor)null, duration);
    }

    public <T> Tween from(T target, int tweenType, TweenAccessor<T> targetAccessor, float duration) {
        Tween tween = this.takeTween();
        this.flushRead();
        tween.setup__(target, tweenType, targetAccessor, duration);
        tween.ease__(TweenEquations.Quad_InOut);
        tween.path__(TweenPaths.CatmullRom);
        tween.isFrom = true;
        this.flushWrite();
        return tween;
    }

    public <T> Tween set(T target, int tweenType) {
        return this.set(target, tweenType, (TweenAccessor)null);
    }

    public <T> Tween set(T target, int tweenType, TweenAccessor<T> targetAccessor) {
        Tween tween = this.takeTween();
        this.flushRead();
        tween.setup__(target, tweenType, targetAccessor, 0.0F);
        tween.ease__(TweenEquations.Quad_In);
        this.flushWrite();
        return tween;
    }

    public Tween call(TweenCallback callback) {
        Tween tween = this.takeTween();
        this.flushRead();
        tween.setup__((Object)null, -1, (TweenAccessor)null, 0.0F);
        callback.triggers = 2;
        tween.addCallback(callback);
        this.flushWrite();
        return tween;
    }

    public Tween mark() {
        Tween tween = this.takeTween();
        tween.setup__((Object)null, -1, (TweenAccessor)null, 0.0F);
        this.flushWrite();
        return tween;
    }

    Tween mark__() {
        Tween tween = this.takeTween__();
        tween.setup__((Object)null, -1, (TweenAccessor)null, 0.0F);
        return tween;
    }

    Timeline takeTimeline() {
        Timeline take = (Timeline)this.poolTimeline.take();
        this.flushRead();
        return take;
    }

    Tween takeTween() {
        Tween take = (Tween)this.poolTween.take();
        this.flushRead();
        return take;
    }

    Tween takeTween__() {
        return (Tween)this.poolTween.take();
    }

    void free(Timeline timeline) {
        this.poolTimeline.put(timeline);
    }

    void free(Tween tween) {
        this.poolTween.put(tween);
    }

    boolean containsAccessor(Class<?> accessorClass) {
        return this.registeredAccessors.containsKey(accessorClass);
    }

    TweenAccessor getAccessor(Class<?> accessorClass) {
        return this.registeredAccessors.get(accessorClass);
    }

    private static int getTweensCount(List<BaseTween<?>> objs) {
        int count = 0;
        int i = 0;

        for(int n = objs.size(); i < n; ++i) {
            BaseTween<?> obj = objs.get(i);
            if (obj instanceof Tween) {
                ++count;
            } else {
                count += getTweensCount(((com.github.q16695.notifications.Timeline)obj).getChildren());
            }
        }

        return count;
    }

    private static int getTimelinesCount(List<BaseTween<?>> objs) {
        int count = 0;
        int i = 0;

        for(int n = objs.size(); i < n; ++i) {
            BaseTween<?> obj = (BaseTween)objs.get(i);
            if (obj instanceof Timeline) {
                count += 1 + getTimelinesCount(((Timeline)obj).getChildren());
            }
        }

        return count;
    }
}

