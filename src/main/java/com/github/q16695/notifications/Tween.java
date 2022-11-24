package com.github.q16695.notifications;

public final class Tween extends BaseTween<Tween> {
    public static final int INFINITY = -1;
    private final int combinedAttrsLimit;
    private final int waypointsLimit;
    private Object target;
    private Class<?> targetClass;
    private TweenAccessor accessor;
    private int type;
    private TweenEquation equation;
    private TweenPath path;
    boolean isFrom;
    private boolean isRelative;
    private int combinedAttrsCnt;
    private int waypointsCount;
    private final float[] startValues;
    private final float[] targetValues;
    private final float[] waypoints;
    private final float[] accessorBuffer;
    private final float[] pathBuffer;

    Tween(TweenEngine animator, int combinedAttrsLimit, int waypointsLimit) {
        super(animator);
        this.combinedAttrsLimit = combinedAttrsLimit;
        this.waypointsLimit = waypointsLimit;
        this.startValues = new float[combinedAttrsLimit];
        this.targetValues = new float[combinedAttrsLimit];
        this.waypoints = new float[waypointsLimit * combinedAttrsLimit];
        this.accessorBuffer = new float[combinedAttrsLimit];
        this.pathBuffer = new float[(2 + waypointsLimit) * combinedAttrsLimit];
        this.destroy();
    }

    protected void destroy() {
        super.destroy();
        this.target = null;
        this.targetClass = null;
        this.accessor = null;
        this.type = -1;
        this.equation = null;
        this.path = null;
        this.isFrom = this.isRelative = false;
        this.combinedAttrsCnt = this.waypointsCount = 0;
        int i = 0;

        int n;
        for(n = this.startValues.length; i < n; ++i) {
            this.startValues[i] = 0.0F;
        }

        i = 0;

        for(n = this.targetValues.length; i < n; ++i) {
            this.targetValues[i] = 0.0F;
        }

        i = 0;

        for(n = this.waypoints.length; i < n; ++i) {
            this.waypoints[i] = 0.0F;
        }

        i = 0;

        for(n = this.accessorBuffer.length; i < n; ++i) {
            this.accessorBuffer[i] = 0.0F;
        }

        i = 0;

        for(n = this.pathBuffer.length; i < n; ++i) {
            this.pathBuffer[i] = 0.0F;
        }

    }

    void setup__(Object target, int tweenType, TweenAccessor targetAccessor, float duration) {
        if (duration < 0.0F) {
            throw new RuntimeException("Duration can not be negative");
        } else {
            this.target = target;
            if (targetAccessor != null) {
                this.accessor = targetAccessor;
            } else {
                this.targetClass = target != null ? this.findTargetClass__() : null;
            }

            this.type = tweenType;
            this.duration = duration;
            this.setup__();
        }
    }

    private Class<?> findTargetClass__() {
        Object target = this.target;
        if (target instanceof TweenAccessor) {
            return target.getClass();
        } else if (this.animator.containsAccessor(target.getClass())) {
            return target.getClass();
        } else {
            Class parentClass;
            for(parentClass = target.getClass().getSuperclass(); parentClass != null && !this.animator.containsAccessor(parentClass); parentClass = parentClass.getSuperclass()) {
            }

            return parentClass;
        }
    }

    public Tween ease(TweenEquation easeEquation) {
        this.equation = easeEquation;
        this.animator.flushWrite();
        return this;
    }

    public Tween ease(TweenEquations easeEquation) {
        this.ease__(easeEquation);
        this.animator.flushWrite();
        return this;
    }

    protected Tween ease__(TweenEquations easeEquation) {
        this.equation = easeEquation.getEquation();
        return this;
    }

    public Tween cast(Class<?> targetClass) {
        this.animator.flushRead();
        if (this.isInitialized) {
            throw new RuntimeException("You can't cast the target of a tween once it has been initialized");
        } else {
            this.targetClass = targetClass;
            this.animator.flushWrite();
            return this;
        }
    }

    public Tween target(float targetValue) {
        this.targetValues[0] = targetValue;
        this.animator.flushWrite();
        return this;
    }

    public Tween target(float targetValue1, float targetValue2) {
        this.targetValues[0] = targetValue1;
        this.targetValues[1] = targetValue2;
        this.animator.flushWrite();
        return this;
    }

    public Tween target(float targetValue1, float targetValue2, float targetValue3) {
        this.targetValues[0] = targetValue1;
        this.targetValues[1] = targetValue2;
        this.targetValues[2] = targetValue3;
        this.animator.flushWrite();
        return this;
    }

    public Tween target(float... targetValues) {
        this.animator.flushRead();
        int length = targetValues.length;
        this.verifyCombinedAttrs(length);
        System.arraycopy(targetValues, 0, this.targetValues, 0, length);
        this.animator.flushWrite();
        return this;
    }

    public Tween targetRelative(float targetValue) {
        this.animator.flushRead();
        this.isRelative = true;
        this.targetValues[0] = this.isInitialized ? targetValue + this.startValues[0] : targetValue;
        this.animator.flushWrite();
        return this;
    }

    public Tween targetRelative(float targetValue1, float targetValue2) {
        this.animator.flushRead();
        this.isRelative = true;
        boolean initialized = this.isInitialized;
        this.targetValues[0] = initialized ? targetValue1 + this.startValues[0] : targetValue1;
        this.targetValues[1] = initialized ? targetValue2 + this.startValues[1] : targetValue2;
        this.animator.flushWrite();
        return this;
    }

    public Tween targetRelative(float targetValue1, float targetValue2, float targetValue3) {
        this.animator.flushRead();
        this.isRelative = true;
        boolean initialized = this.isInitialized;
        float[] startValues = this.startValues;
        this.targetValues[0] = initialized ? targetValue1 + startValues[0] : targetValue1;
        this.targetValues[1] = initialized ? targetValue2 + startValues[1] : targetValue2;
        this.targetValues[2] = initialized ? targetValue3 + startValues[2] : targetValue3;
        this.animator.flushWrite();
        return this;
    }

    public Tween targetRelative(float... targetValues) {
        this.animator.flushRead();
        int length = targetValues.length;
        this.verifyCombinedAttrs(length);
        boolean initialized = this.isInitialized;
        float[] startValues = this.startValues;

        for(int i = 0; i < length; ++i) {
            this.targetValues[i] = initialized ? targetValues[i] + startValues[i] : targetValues[i];
        }

        this.isRelative = true;
        this.animator.flushWrite();
        return this;
    }

    public Tween waypoint(float targetValue) {
        this.animator.flushRead();
        int waypointsCount = this.waypointsCount;
        this.verifyWaypoints(waypointsCount);
        this.waypoints[waypointsCount] = targetValue;
        ++this.waypointsCount;
        this.animator.flushWrite();
        return this;
    }

    public Tween waypoint(float targetValue1, float targetValue2) {
        this.animator.flushRead();
        int waypointsCount = this.waypointsCount;
        this.verifyWaypoints(waypointsCount);
        int count = waypointsCount * 2;
        float[] waypoints = this.waypoints;
        waypoints[count] = targetValue1;
        waypoints[count + 1] = targetValue2;
        ++this.waypointsCount;
        this.animator.flushWrite();
        return this;
    }

    public Tween waypoint(float targetValue1, float targetValue2, float targetValue3) {
        this.animator.flushRead();
        int waypointsCount = this.waypointsCount;
        this.verifyWaypoints(waypointsCount);
        int count = waypointsCount * 3;
        float[] waypoints = this.waypoints;
        waypoints[count] = targetValue1;
        waypoints[count + 1] = targetValue2;
        waypoints[count + 2] = targetValue3;
        ++this.waypointsCount;
        this.animator.flushWrite();
        return this;
    }

    public Tween waypoint(float... targetValues) {
        this.animator.flushRead();
        int waypointsCount = this.waypointsCount;
        this.verifyWaypoints(waypointsCount);
        System.arraycopy(targetValues, 0, this.waypoints, waypointsCount * targetValues.length, targetValues.length);
        ++this.waypointsCount;
        this.animator.flushWrite();
        return this;
    }

    public Tween path(TweenPaths path) {
        this.path__(path);
        this.animator.flushWrite();
        return this;
    }

    protected Tween path__(TweenPaths path) {
        this.path = path.path();
        return this;
    }

    public Tween path(TweenPath path) {
        this.path__(path);
        this.animator.flushWrite();
        return this;
    }

    protected Tween path__(TweenPath path) {
        this.path = path;
        return this;
    }

    public Object getTarget() {
        this.animator.flushRead();
        return this.target;
    }

    public int getType() {
        this.animator.flushRead();
        return this.type;
    }

    public TweenEquation getEasing() {
        this.animator.flushRead();
        return this.equation;
    }

    public float[] getTargetValues() {
        this.animator.flushRead();
        return this.targetValues;
    }

    public int getCombinedAttributesCount() {
        this.animator.flushRead();
        return this.combinedAttrsCnt;
    }

    public TweenAccessor<?> getAccessor() {
        this.animator.flushRead();
        return this.accessor;
    }

    public Class<?> getTargetClass() {
        this.animator.flushRead();
        return this.targetClass;
    }

    public Tween startUnmanaged() {
        this.animator.flushRead();
        this.startUnmanaged__();
        this.animator.flushWrite();
        return this;
    }

    public void startUnmanaged__() {
        super.startUnmanaged__();
        Object target = this.target;
        if (target != null) {
            if (this.accessor == null) {
                if (target instanceof TweenAccessor) {
                    this.accessor = (TweenAccessor)target;
                } else {
                    this.accessor = this.animator.getAccessor(this.targetClass);
                }
            }

            if (this.accessor != null) {
                this.combinedAttrsCnt = this.accessor.getValues(target, this.type, this.accessorBuffer);
                this.verifyCombinedAttrs(this.combinedAttrsCnt);
            } else {
                throw new NullPointerException("No TweenAccessor was found for the target");
            }
        }
    }

    public void free() {
        this.animator.free(this);
    }

    protected void setValues(boolean updateDirection, boolean updateValue) {
        if (this.target != null && this.isInitialized && !this.isCanceled) {
            if (updateValue) {
                this.accessor.setValues(this.target, this.type, this.startValues);
            } else if (this.canAutoReverse && (this.repeatCountOrig & 1) != 0) {
                this.accessor.setValues(this.target, this.type, this.startValues);
            } else {
                this.accessor.setValues(this.target, this.type, this.targetValues);
            }

        }
    }

    protected void initializeValues() {
        Object target = this.target;
        if (target != null && !this.isCanceled) {
            float[] startValues = this.startValues;
            float[] targetValues = this.targetValues;
            int combinedAttrsCnt = this.combinedAttrsCnt;
            this.accessor.getValues(target, this.type, startValues);
            int waypointsCount;
            if (this.isRelative) {
                waypointsCount = this.waypointsCount;
                float[] waypoints = this.waypoints;
                int i;
                int ii;
                if (this.isFrom) {
                    for(i = 0; i < combinedAttrsCnt; ++i) {
                        targetValues[i] += startValues[i];

                        for(ii = 0; ii < waypointsCount; ++ii) {
                            waypoints[ii * combinedAttrsCnt + i] += startValues[i];
                        }

                        float tmp = startValues[i];
                        startValues[i] = targetValues[i];
                        targetValues[i] = tmp;
                    }
                } else {
                    for(i = 0; i < combinedAttrsCnt; ++i) {
                        targetValues[i] += startValues[i];

                        for(ii = 0; ii < waypointsCount; ++ii) {
                            waypoints[ii * combinedAttrsCnt + i] += startValues[i];
                        }
                    }
                }
            } else if (this.isFrom) {
                for(waypointsCount = 0; waypointsCount < combinedAttrsCnt; ++waypointsCount) {
                    float tmp = startValues[waypointsCount];
                    startValues[waypointsCount] = targetValues[waypointsCount];
                    targetValues[waypointsCount] = tmp;
                }
            }

        }
    }

    protected void update(boolean updateDirection, float delta) {
        Object target = this.target;
        TweenEquation equation = this.equation;
        if (target != null && equation != null && this.isInitialized && !this.isCanceled) {
            float duration = this.duration;
            float time = this.currentTime;
            float tweenValue = equation.compute(time / duration);
            float[] accessorBuffer = this.accessorBuffer;
            int combinedAttrsCnt = this.combinedAttrsCnt;
            int waypointsCnt = this.waypointsCount;
            TweenPath path = this.path;
            float[] startValues = this.startValues;
            float[] targetValues = this.targetValues;
            if (waypointsCnt != 0 && path != null) {
                float[] waypoints = this.waypoints;
                float[] pathBuffer = this.pathBuffer;

                for(int i = 0; i < combinedAttrsCnt; ++i) {
                    pathBuffer[0] = startValues[i];
                    pathBuffer[1 + waypointsCnt] = targetValues[i];

                    for(int ii = 0; ii < waypointsCnt; ++ii) {
                        pathBuffer[ii + 1] = waypoints[ii * combinedAttrsCnt + i];
                    }

                    accessorBuffer[i] = path.compute(tweenValue, pathBuffer, waypointsCnt + 2);
                }
            } else {
                for(int i = 0; i < combinedAttrsCnt; ++i) {
                    accessorBuffer[i] = startValues[i] + tweenValue * (targetValues[i] - startValues[i]);
                }
            }

            this.accessor.setValues(target, this.type, accessorBuffer);
        }
    }

    protected boolean containsTarget(Object target) {
        return this.target == target;
    }

    protected boolean containsTarget(Object target, int tweenType) {
        return this.target == target && this.type == tweenType;
    }

    void verifyCombinedAttrs(int length) {
        if (length > this.combinedAttrsLimit) {
            String msg = "You cannot combine more than " + this.combinedAttrsLimit + " attributes in a tween. You can raise this limit with Tween.setCombinedAttributesLimit(), which should be called once in application initialization code.";
            throw new RuntimeException(msg);
        }
    }

    void verifyWaypoints(int waypointsCount) {
        if (waypointsCount == this.waypointsLimit) {
            String msg = "You cannot add more than " + this.waypointsLimit + " waypoints to a tween. You can raise this limit with Tween.setWaypointsLimit(), which should be called once in application initialization code.";
            throw new RuntimeException(msg);
        }
    }
}
