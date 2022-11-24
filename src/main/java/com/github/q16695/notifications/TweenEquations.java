package com.github.q16695.notifications;

public enum TweenEquations {
    Linear(new TweenEquation() {
        public float compute(float time) {
            return time;
        }

        public String toString() {
            return "Linear.INOUT";
        }
    }),
    Quad_In(new TweenEquation() {
        public final float compute(float time) {
            return time * time;
        }

        public String toString() {
            return "Quad.IN";
        }
    }),
    Quad_Out(new TweenEquation() {
        public final float compute(float time) {
            float m = time - 1.0F;
            return 1.0F - m * m;
        }

        public String toString() {
            return "Quad.OUT";
        }
    }),
    Quad_InOut(new TweenEquation() {
        public final float compute(float time) {
            float t = time * 2.0F;
            if (t < 1.0F) {
                return time * t;
            } else {
                float m = time - 1.0F;
                return 1.0F - m * m * 2.0F;
            }
        }

        public String toString() {
            return "Quad.INOUT";
        }
    }),
    Cubic_In(new TweenEquation() {
        public float compute(float time) {
            return time * time * time;
        }

        public String toString() {
            return "Cubic.IN";
        }
    }),
    Cubic_Out(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            return 1.0F + m * m * m;
        }

        public String toString() {
            return "Cubic.OUT";
        }
    }),
    Cubic_InOut(new TweenEquation() {
        public float compute(float time) {
            float t = time * 2.0F;
            if (t < 1.0F) {
                return time * t * t;
            } else {
                float m = time - 1.0F;
                return 1.0F + m * m * m * 4.0F;
            }
        }

        public String toString() {
            return "Cubic.INOUT";
        }
    }),
    Quart_In(new TweenEquation() {
        public float compute(float time) {
            float t = time * time;
            return t * t;
        }

        public String toString() {
            return "Quart.IN";
        }
    }),
    Quart_Out(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            float m2 = m * m;
            return 1.0F - m2 * m2;
        }

        public String toString() {
            return "Quart.OUT";
        }
    }),
    Quart_InOut(new TweenEquation() {
        public float compute(float time) {
            float t = time * 2.0F;
            if (t < 1.0F) {
                return time * t * t * t;
            } else {
                float m = time - 1.0F;
                float m2 = m * m;
                return 1.0F - m2 * m2 * 8.0F;
            }
        }

        public String toString() {
            return "Quart.INOUT";
        }
    }),
    Quint_In(new TweenEquation() {
        public float compute(float time) {
            float t = time * time;
            return t * t * time;
        }

        public String toString() {
            return "Quint.IN";
        }
    }),
    Quint_Out(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            float m2 = m * m;
            return 1.0F + m2 * m2 * m;
        }

        public String toString() {
            return "Quint.OUT";
        }
    }),
    Quint_InOut(new TweenEquation() {
        public float compute(float time) {
            float t = time * 2.0F;
            float m;
            if (t < 1.0F) {
                m = t * t;
                return time * m * m;
            } else {
                m = time - 1.0F;
                float m2 = m * m;
                return 1.0F + m2 * m2 * m * 16.0F;
            }
        }

        public String toString() {
            return "Quint.INOUT";
        }
    }),
    Sextic_In(new TweenEquation() {
        public float compute(float time) {
            float t = time * time;
            return t * t * t;
        }

        public String toString() {
            return "Sextic.IN";
        }
    }),
    Sextic_Out(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            float m2 = m * m;
            return 1.0F - m2 * m2 * m2;
        }

        public String toString() {
            return "Sextic.OUT";
        }
    }),
    Sextic_InOut(new TweenEquation() {
        public float compute(float time) {
            float t = time * 2.0F;
            float m;
            if (t < 1.0F) {
                m = t * t;
                return time * m * m * t;
            } else {
                m = time - 1.0F;
                float m2 = m * m;
                return 1.0F - m2 * m2 * m2 * 32.0F;
            }
        }

        public String toString() {
            return "Sextic.INOUT";
        }
    }),
    Septic_In(new TweenEquation() {
        public float compute(float time) {
            float t = time * time;
            return t * t * t * time;
        }

        public String toString() {
            return "Septic.IN";
        }
    }),
    Septic_Out(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            float m2 = m * m;
            return 1.0F + m2 * m2 * m2 * m;
        }

        public String toString() {
            return "Septic.OUT";
        }
    }),
    Septic_InOut(new TweenEquation() {
        public float compute(float time) {
            float t = time * 2.0F;
            float m;
            if (t < 1.0F) {
                m = t * t;
                return time * m * m * m;
            } else {
                m = time - 1.0F;
                float m2 = m * m;
                return 1.0F + m2 * m2 * m2 * m * 64.0F;
            }
        }

        public String toString() {
            return "Septic.INOUT";
        }
    }),
    Octic_In(new TweenEquation() {
        public float compute(float time) {
            float t = time * time;
            float t2 = t * t;
            return t2 * t2;
        }

        public String toString() {
            return "Octic.IN";
        }
    }),
    Octic_Out(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            float m2 = m * m;
            float m4 = m2 * m2;
            return 1.0F - m4 * m4;
        }

        public String toString() {
            return "Octic.OUT";
        }
    }),
    Octic_InOut(new TweenEquation() {
        public float compute(float time) {
            float t = time * 2.0F;
            float m;
            if (t < 1.0F) {
                m = t * t;
                return time * m * m * m * t;
            } else {
                m = time - 1.0F;
                float m2 = m * m;
                float m4 = m2 * m2;
                return 1.0F - m4 * m4 * 128.0F;
            }
        }

        public String toString() {
            return "Octic.INOUT";
        }
    }),
    Circle_In(new TweenEquation() {
        public final float compute(float time) {
            return (float)(1.0D - Math.sqrt((double)(1.0F - time * time)));
        }

        public String toString() {
            return "Circle.IN";
        }
    }),
    Circle_Out(new TweenEquation() {
        public final float compute(float time) {
            float m = time - 1.0F;
            return (float)Math.sqrt((double)(1.0F - m * m));
        }

        public String toString() {
            return "Circle.OUT";
        }
    }),
    Circle_InOut(new TweenEquation() {
        public final float compute(float time) {
            float m = time - 1.0F;
            float t = time * 2.0F;
            return t < 1.0F ? (float)((1.0D - Math.sqrt((double)(1.0F - t * t))) * 0.5D) : (float)((Math.sqrt((double)(1.0F - 4.0F * m * m)) + 1.0D) * 0.5D);
        }

        public String toString() {
            return "Circle.INOUT";
        }
    }),
    Sine_In(new TweenEquation() {
        public float compute(float time) {
            return (float)(1.0D - Math.cos((double)(time * 1.5707964F)));
        }

        public String toString() {
            return "Sine.IN";
        }
    }),
    Sine_Out(new TweenEquation() {
        public float compute(float time) {
            return (float)Math.sin((double)(time * 1.5707964F));
        }

        public String toString() {
            return "Sine.OUT";
        }
    }),
    Sine_InOut(new TweenEquation() {
        public float compute(float time) {
            return (float)(0.5D * (1.0D - Math.cos((double)(time * 3.1415927F))));
        }

        public String toString() {
            return "Sine.INOUT";
        }
    }),
    Expo_In(new TweenEquation() {
        public float compute(float time) {
            return (float)Math.pow(2.0D, (double)(10.0F * (time - 1.0F)));
        }

        public String toString() {
            return "Expo.IN";
        }
    }),
    Expo_Out(new TweenEquation() {
        public float compute(float time) {
            return (float)(1.0D - Math.pow(2.0D, (double)(-10.0F * time)));
        }

        public String toString() {
            return "Expo.OUT";
        }
    }),
    Expo_InOut(new TweenEquation() {
        public float compute(float time) {
            return time < 0.5F ? (float)Math.pow(2.0D, (double)(10.0F * (2.0F * time - 1.0F) - 1.0F)) : (float)(1.0D - Math.pow(2.0D, (double)(-10.0F * (2.0F * time - 1.0F) - 1.0F)));
        }

        public String toString() {
            return "Expo.INOUT";
        }
    }),
    Back_In(new TweenEquation() {
        public final float compute(float time) {
            return time * time * (time * 2.70158F - 1.70158F);
        }

        public String toString() {
            return "Back.IN";
        }
    }),
    Back_Out(new TweenEquation() {
        public final float compute(float time) {
            float m = time - 1.0F;
            return 1.0F + m * m * (m * 2.70158F + 1.70158F);
        }

        public String toString() {
            return "Back.OUT";
        }
    }),
    Back_InOut(new TweenEquation() {
        public final float compute(float time) {
            float m = time - 1.0F;
            float t = time * 2.0F;
            return time < 0.5F ? time * t * (t * 3.5949094F - 2.5949094F) : 1.0F + 2.0F * m * m * (2.0F * m * 3.5949094F + 2.5949094F);
        }

        public String toString() {
            return "Back.INOUT";
        }
    }),
    Bounce_In(new TweenEquation() {
        public final float compute(float time) {
            return 1.0F - TweenEquations.Bounce_Out.equation.compute(1.0F - time);
        }

        public String toString() {
            return "Bounce.IN";
        }
    }),
    Bounce_Out(new TweenEquation() {
        public final float compute(float time) {
            if (time < 0.36363637F) {
                return 7.5625F * time * time;
            } else {
                float t;
                if (time < 0.72727275F) {
                    t = time - 0.54545456F;
                    return 7.5625F * t * t + 0.75F;
                } else if (time < 0.90909094F) {
                    t = time - 0.8181819F;
                    return 7.5625F * t * t + 0.9375F;
                } else {
                    t = time - 0.9545455F;
                    return 7.5625F * t * t + 0.984375F;
                }
            }
        }

        public String toString() {
            return "Bounce.OUT";
        }
    }),
    Bounce_InOut(new TweenEquation() {
        public final float compute(float time) {
            float t = time * 2.0F;
            return t < 1.0F ? 0.5F - 0.5F * TweenEquations.Bounce_Out.equation.compute(1.0F - t) : 0.5F + 0.5F * TweenEquations.Bounce_Out.equation.compute(t - 1.0F);
        }

        public String toString() {
            return "Bounce.INOUT";
        }
    }),
    Elastic_In(new TweenEquation() {
        public float compute(float time) {
            float m = time - 1.0F;
            return (float)(-Math.pow(2.0D, (double)(10.0F * m)) * Math.sin((double)((m * 40.0F - 3.0F) * 0.5235988F)));
        }

        public String toString() {
            return "Elastic.IN";
        }
    }),
    Elastic_Out(new TweenEquation() {
        public float compute(float time) {
            return (float)(1.0D + Math.pow(2.0D, (double)(10.0F * -time)) * Math.sin((double)((-time * 40.0F - 3.0F) * 0.5235988F)));
        }

        public String toString() {
            return "Elastic.OUT";
        }
    }),
    Elastic_InOut(new TweenEquation() {
        public float compute(float time) {
            time *= 2.0F;
            --time;
            float k = (80.0F * time - 9.0F) * 0.17453292F;
            return time < 0.0F ? (float)(-0.5D * Math.pow(2.0D, (double)(10.0F * time)) * Math.sin((double)k)) : (float)(1.0D + 0.5D * Math.pow(2.0D, (double)(-10.0F * time)) * Math.sin((double)k));
        }

        public String toString() {
            return "Elastic.INOUT";
        }
    });

    private static final float PI = 3.1415927F;
    private static final float PI_DIV_2 = 1.5707964F;
    private static final float PI_DIV_6 = 0.5235988F;
    private static final float PI_DIV_18 = 0.17453292F;
    private static final float K = 1.70158F;
    private static final float K2 = 2.5949094F;
    private static final float bounce_r = 0.36363637F;
    private static final float bounce_k0 = 7.5625F;
    private static final float bounce_k1 = 0.36363637F;
    private static final float bounce_k2 = 0.72727275F;
    private static final float bounce_k3 = 0.54545456F;
    private static final float bounce_k4 = 0.90909094F;
    private static final float bounce_k5 = 0.8181819F;
    private static final float bounce_k6 = 0.9545455F;
    private final transient TweenEquation equation;

    public static TweenEquation parse(String name) {
        TweenEquations[] values = values();
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            if (name.equals(values[i].toString())) {
                return values[i].equation;
            }
        }

        return null;
    }

    private TweenEquations(TweenEquation equation) {
        this.equation = equation;
    }

    public TweenEquation getEquation() {
        return this.equation;
    }

    public float compute(float time) {
        return this.equation.compute(time);
    }

    public String toString() {
        return this.equation.toString();
    }
}
