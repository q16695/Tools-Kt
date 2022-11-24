package com.github.q16695.notifications;

public enum TweenPaths {
    Linear(new TweenPath() {
        public float compute(float tweenValue, float[] points, int pointsCount) {
            int segment = (int)Math.floor((double)((float)(pointsCount - 1) * tweenValue));
            segment = Math.max(segment, 0);
            segment = Math.min(segment, pointsCount - 2);
            tweenValue = tweenValue * (float)(pointsCount - 1) - (float)segment;
            return points[segment] + tweenValue * (points[segment + 1] - points[segment]);
        }
    }),
    CatmullRom(new TweenPath() {
        public float compute(float tweenValue, float[] points, int pointsCount) {
            int segment = (int)Math.floor((double)((float)(pointsCount - 1) * tweenValue));
            segment = Math.max(segment, 0);
            segment = Math.min(segment, pointsCount - 2);
            tweenValue = tweenValue * (float)(pointsCount - 1) - (float)segment;
            if (segment == 0) {
                return this.catmullRomSpline(points[0], points[0], points[1], points[2], tweenValue);
            } else {
                return segment == pointsCount - 2 ? this.catmullRomSpline(points[pointsCount - 3], points[pointsCount - 2], points[pointsCount - 1], points[pointsCount - 1], tweenValue) : this.catmullRomSpline(points[segment - 1], points[segment], points[segment + 1], points[segment + 2], tweenValue);
            }
        }

        private float catmullRomSpline(float a, float b, float c, float d, float t) {
            float t1 = (c - a) * 0.5F;
            float t2 = (d - b) * 0.5F;
            float _t2 = t * t;
            float _t3 = _t2 * t;
            float _2t3 = 2.0F * _t3;
            float _3t2 = 3.0F * _t2;
            float h1 = _2t3 - _3t2 + 1.0F;
            float h2 = -_2t3 + _3t2;
            float h3 = _t3 - 2.0F * _t2 + t;
            float h4 = _t3 - _t2;
            return b * h1 + c * h2 + t1 * h3 + t2 * h4;
        }
    });

    private final transient TweenPath path;

    private TweenPaths(TweenPath path) {
        this.path = path;
    }

    public TweenPath path() {
        return this.path;
    }
}

