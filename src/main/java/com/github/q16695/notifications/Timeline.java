package com.github.q16695.notifications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Timeline extends BaseTween<Timeline> {
    private final List<BaseTween<?>> children = new ArrayList<>(10);
    private BaseTween<?>[] childrenArray = null;
    private int childrenSize;
    private int childrenSizeMinusOne;
    private Mode mode;
    private Timeline parent;
    private BaseTween<?> current;
    private int currentIndex;

    Timeline(TweenEngine animator) {
        super(animator);
        this.destroy();
    }

    protected void reset() {
        super.reset();
        this.currentIndex = 0;
        this.current = this.childrenArray[0];
        int i = 0;

        for(int n = this.childrenArray.length; i < n; ++i) {
            BaseTween<?> tween = this.childrenArray[i];
            tween.reset();
        }

    }

    protected void destroy() {
        super.destroy();
        this.children.clear();
        this.childrenArray = null;
        this.current = this.parent = null;
        this.currentIndex = 0;
    }

    void setup__(Mode mode) {
        this.mode = mode;
        this.current = this;
    }

    public Timeline push(Tween tween) {
        tween.startUnmanaged();
        this.children.add(tween);
        this.setupTimeline__(tween);
        this.animator.flushWrite();
        return this;
    }

    public Timeline push(Timeline timeline) {
        this.animator.flushRead();
        timeline.parent = this;
        this.children.add(timeline);
        this.setupTimeline__(timeline);
        this.animator.flushWrite();
        return this;
    }

    public Timeline pushPause(float time) {
        if (time < 0.0F) {
            throw new RuntimeException("You can't push a negative pause to a timeline. Just make the last entry's duration shorter or use with a parallel timeline and appropriate delays in place.");
        } else {
            Tween tween = this.animator.mark__();
            this.animator.flushRead();
            tween.delay__(time);
            tween.startUnmanaged__();
            this.children.add(tween);
            this.setupTimeline__(tween);
            this.animator.flushWrite();
            return this;
        }
    }

    public Timeline beginSequential() {
        Timeline timeline = this.animator.takeTimeline();
        this.children.add(timeline);
        timeline.parent = this;
        timeline.mode = Mode.SEQUENTIAL;
        this.current = timeline;
        return timeline;
    }

    public Timeline beginParallel() {
        Timeline timeline = this.animator.takeTimeline();
        this.animator.flushRead();
        this.children.add(timeline);
        timeline.parent = this;
        timeline.mode = Mode.PARALLEL;
        this.current = timeline;
        return timeline;
    }

    public Timeline end() {
        if (this.current == this) {
            throw new RuntimeException("Nothing to end, calling end before begin!");
        } else {
            this.parent.setupTimeline__(this);
            this.current = this.parent;
            if (this.current == null) {
                throw new RuntimeException("Whoops! Shouldn't be null!");
            } else if (this.current.getClass() != Timeline.class) {
                throw new RuntimeException("You cannot end something other than a Timeline!");
            } else {
                this.animator.flushWrite();
                return (Timeline)this.current;
            }
        }
    }

    private void setupTimeline__(BaseTween<?> tweenOrTimeline) {
        switch(this.mode) {
            case SEQUENTIAL:
                this.duration += tweenOrTimeline.getFullDuration__();
                break;
            case PARALLEL:
                this.duration = Math.max(this.duration, tweenOrTimeline.getFullDuration__());
        }

        this.childrenSize = this.children.size();
        this.childrenSizeMinusOne = this.childrenSize - 1;
        this.childrenArray = new BaseTween[this.childrenSize];
        this.children.toArray(this.childrenArray);
        if (this.childrenSize > 0) {
            this.current = this.childrenArray[0];
        } else {
            throw new RuntimeException("Creating a timeline with zero children. This is likely unintended, and is not permitted.");
        }
    }

    public List<BaseTween<?>> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public Timeline startUnmanaged() {
        super.startUnmanaged();

        for(int i = 0; i < this.childrenSize; ++i) {
            BaseTween<?> obj = this.childrenArray[i];
            if (obj.repeatCountOrig < 0) {
                throw new RuntimeException("You can't push an object with infinite repetitions in a timeline");
            }

            obj.startUnmanaged();
        }

        return this;
    }

    void startUnmanaged__() {
        super.startUnmanaged__();

        for(int i = 0; i < this.childrenSize; ++i) {
            BaseTween<?> obj = this.childrenArray[i];
            if (obj.repeatCountOrig < 0) {
                throw new RuntimeException("You can't push an object with infinite repetitions in a timeline");
            }

            obj.startUnmanaged__();
        }

    }

    public void free() {
        for(int i = this.children.size() - 1; i >= 0; --i) {
            BaseTween<?> tween = this.children.remove(i);
            if (tween.isAutoRemoveEnabled) {
                tween.free();
            }
        }

        this.animator.free(this);
    }

    protected void adjustForRepeat_AutoReverse(boolean newDirection) {
        super.adjustForRepeat_AutoReverse(newDirection);
        int i = 0;

        for(int n = this.childrenArray.length; i < n; ++i) {
            BaseTween<?> tween = this.childrenArray[i];
            tween.adjustForRepeat_AutoReverse(newDirection);
        }

    }

    protected void adjustForRepeat_Linear(boolean newDirection) {
        super.adjustForRepeat_Linear(newDirection);
        int i = 0;

        for(int n = this.childrenArray.length; i < n; ++i) {
            BaseTween<?> tween = this.childrenArray[i];
            tween.adjustForRepeat_Linear(newDirection);
        }

        if (this.mode == Mode.SEQUENTIAL) {
            if (newDirection) {
                this.currentIndex = 0;
            } else {
                this.currentIndex = this.childrenSize - 1;
            }

            this.current = this.childrenArray[this.currentIndex];
        }

    }

    protected void update(boolean updateDirection, float delta) {
        if (this.mode == Mode.SEQUENTIAL) {
            if (updateDirection) {
                while(delta != 0.0F) {
                    delta = this.current.update__(delta);
                    if (this.current.state == 3) {
                        if (this.currentIndex < this.childrenSizeMinusOne) {
                            ++this.currentIndex;
                            this.current = this.childrenArray[this.currentIndex];
                        } else if (this.parent != null) {
                            return;
                        }
                    }
                }
            } else {
                while(delta != 0.0F) {
                    delta = this.current.update__(delta);
                    if (this.current.state == 3) {
                        if (this.currentIndex > 0) {
                            --this.currentIndex;
                            this.current = this.childrenArray[this.currentIndex];
                        } else if (this.parent != null) {
                            return;
                        }
                    }
                }
            }
        } else {
            int i;
            if (updateDirection) {
                i = 0;

                for(int n = this.childrenArray.length; i < n; ++i) {
                    BaseTween<?> tween = this.childrenArray[i];
                    float returned = tween.update__(delta);
                    if (tween.state == 3) {
                        tween.currentTime += returned;
                    }
                }
            } else {
                for(i = this.childrenArray.length - 1; i >= 0; --i) {
                    BaseTween<?> tween = this.childrenArray[i];
                    float returned = tween.update__(delta);
                    if (tween.state == 3) {
                        tween.currentTime += returned;
                    }
                }
            }
        }

    }

    protected void setValues(boolean updateDirection, boolean updateValue) {
        int i;
        if (updateDirection) {
            i = 0;

            for(int n = this.childrenArray.length; i < n; ++i) {
                BaseTween<?> tween = this.childrenArray[i];
                tween.setValues(true, updateValue);
            }
        } else {
            for(i = this.childrenArray.length - 1; i >= 0; --i) {
                BaseTween<?> tween = this.childrenArray[i];
                tween.setValues(false, updateValue);
            }
        }

    }

    protected final boolean containsTarget(Object target) {
        int i = 0;

        for(int n = this.childrenArray.length; i < n; ++i) {
            BaseTween<?> tween = this.childrenArray[i];
            if (tween.containsTarget(target)) {
                return true;
            }
        }

        return false;
    }

    protected final boolean containsTarget(Object target, int tweenType) {
        int i = 0;

        for(int n = this.childrenArray.length; i < n; ++i) {
            BaseTween<?> tween = this.childrenArray[i];
            if (tween.containsTarget(target, tweenType)) {
                return true;
            }
        }

        return false;
    }

    static enum Mode {
        SEQUENTIAL,
        PARALLEL;

        private Mode() {
        }
    }
}

