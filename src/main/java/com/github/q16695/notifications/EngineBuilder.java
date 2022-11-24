package com.github.q16695.notifications;

import java.util.HashMap;
import java.util.Map;

public class EngineBuilder {
    private boolean threadSafe = true;
    private Map<Class<?>, TweenAccessor<?>> registeredAccessors = new HashMap();
    private int combinedAttrsLimit = 3;
    private int waypointsLimit = 0;

    EngineBuilder() {
    }

    public TweenEngine build() {
        return this.threadSafe ? new TweenEngine(this.threadSafe, this.combinedAttrsLimit, this.waypointsLimit, this.registeredAccessors) : new TweenEngine(this.threadSafe, this.combinedAttrsLimit, this.waypointsLimit, this.registeredAccessors) {
            long flushRead() {
                return 0L;
            }

            void flushWrite() {
            }
        };
    }

    public EngineBuilder unsafe() {
        this.threadSafe = false;
        return this;
    }

    public EngineBuilder setCombinedAttributesLimit(int limit) {
        this.combinedAttrsLimit = limit;
        return this;
    }

    public EngineBuilder setWaypointsLimit(int limit) {
        this.waypointsLimit = limit;
        return this;
    }

    public EngineBuilder registerAccessor(Class<?> someClass, TweenAccessor<?> defaultAccessor) {
        this.registeredAccessors.put(someClass, defaultAccessor);
        return this;
    }

    public int getCombinedAttrsLimit() {
        return this.combinedAttrsLimit;
    }

    public int getWaypointsLimit() {
        return this.waypointsLimit;
    }
}

