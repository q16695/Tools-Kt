package com.github.q16695.notifications;

class NotifyAccessor implements TweenAccessor<LookAndFeel> {
    static final int Y_POS = 1;
    static final int X_Y_POS = 2;
    static final int PROGRESS = 3;

    public int getValues(LookAndFeel target, int tweenType, float[] returnValues) {
        switch(tweenType) {
            case 1:
                returnValues[0] = (float)target.getY();
                return 1;
            case 2:
                returnValues[0] = (float)target.getX();
                returnValues[1] = (float)target.getY();
                return 2;
            case 3:
                returnValues[0] = (float)target.getProgress();
                return 1;
            default:
                return 1;
        }
    }

    public void setValues(LookAndFeel target, int tweenType, float[] newValues) {
        switch(tweenType) {
            case 1:
                target.setY((int)newValues[0]);
                return;
            case 2:
                target.setLocation((int)newValues[0], (int)newValues[1]);
                return;
            case 3:
                target.setProgress((int)newValues[0]);
                return;
            default:
        }
    }
}

