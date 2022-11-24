package com.github.q16695.notifications;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

class LookAndFeel {
    private static final Map<String, PopupList> popups = new HashMap<>();
    static final TweenEngine animation = TweenEngine.create().unsafe().build();
    static final NotifyAccessor accessor = new NotifyAccessor();
    private static final ActionHandlerLong frameStartHandler = new ActionHandlerLong() {
        public void handle(long deltaInNanos) {
            LookAndFeel.animation.update(deltaInNanos);
        }
    };
    static final int SPACER = 10;
    static final int MARGIN = 20;
    private static final com.github.q16695.notifications.WindowAdapter windowListener = new WindowAdapter();
    private static final MouseAdapter mouseListener = new ClickAdapter();
    private static final Random RANDOM = new Random();
    private static final float MOVE_DURATION;
    private final boolean isDesktopNotification;
    private volatile int anchorX;
    private volatile int anchorY;
    private final INotify notify;
    private final Window parent;
    private final NotifyCanvas notifyCanvas;
    private final float hideAfterDurationInSeconds;
    private final Pos position;
    private final String idAndPosition;
    private int popupIndex;
    private volatile Tween tween = null;
    private volatile Tween hideTween = null;
    private final ActionHandler<Notification> onGeneralAreaClickAction;

    LookAndFeel(INotify notify, Window parent, NotifyCanvas notifyCanvas, final Notification notification, Rectangle parentBounds, boolean isDesktopNotification) {
        this.notify = notify;
        this.parent = parent;
        this.notifyCanvas = notifyCanvas;
        this.isDesktopNotification = isDesktopNotification;
        if (isDesktopNotification) {
            parent.addWindowListener(windowListener);
        }

        notifyCanvas.addMouseListener(mouseListener);
        this.hideAfterDurationInSeconds = (float)notification.hideAfterDurationInMillis / 1000.0F;
        this.position = notification.position;
        if (notification.onGeneralAreaClickAction != null) {
            this.onGeneralAreaClickAction = new ActionHandler<Notification>() {
                public void handle(Notification value) {
                    notification.onGeneralAreaClickAction.handle(notification);
                }
            };
        } else {
            this.onGeneralAreaClickAction = null;
        }

        if (isDesktopNotification) {
            Point point = new Point((int)parentBounds.getX(), (int)parentBounds.getY());
            this.idAndPosition = ScreenUtil.getMonitorNumberAtLocation(point) + ":" + this.position;
        } else {
            this.idAndPosition = parent.getName() + ":" + this.position;
        }

        this.anchorX = getAnchorX(this.position, parentBounds, isDesktopNotification);
        this.anchorY = getAnchorY(this.position, parentBounds, isDesktopNotification);
    }

    void onClick(int x, int y) {
        if (!this.notifyCanvas.isCloseButton(x, y) && this.onGeneralAreaClickAction != null) {
            this.onGeneralAreaClickAction.handle(null);
        }

        this.notify.close();
    }

    void reLayout(Rectangle bounds) {
        this.anchorX = getAnchorX(this.position, bounds, this.isDesktopNotification);
        this.anchorY = getAnchorY(this.position, bounds, this.isDesktopNotification);
        boolean showFromTop = isShowFromTop(this);
        if (this.tween != null) {
            this.tween.cancel();
            this.tween = null;
        }

        int changedY;
        if (this.popupIndex == 0) {
            changedY = this.anchorY;
        } else {
            synchronized(popups) {
                String id = this.idAndPosition;
                PopupList looks = (PopupList)popups.get(id);
                if (looks != null) {
                    if (showFromTop) {
                        changedY = this.anchorY + this.popupIndex * 97;
                    } else {
                        changedY = this.anchorY - this.popupIndex * 97;
                    }
                } else {
                    changedY = this.anchorY;
                }
            }
        }

        this.setLocation(this.anchorX, changedY);
    }

    void close() {
        if (this.hideTween != null) {
            this.hideTween.cancel();
            this.hideTween = null;
        }

        if (this.tween != null) {
            this.tween.cancel();
            this.tween = null;
        }

        if (this.isDesktopNotification) {
            this.parent.removeWindowListener(windowListener);
        }

        this.parent.removeMouseListener(mouseListener);
        this.updatePositionsPre(false);
        this.updatePositionsPost(false);
    }

    void shake(int durationInMillis, int amplitude) {
        int i1 = RANDOM.nextInt((amplitude << 2) + 1) - amplitude;
        int i2 = RANDOM.nextInt((amplitude << 2) + 1) - amplitude;
        i1 >>= 2;
        i2 >>= 2;
        if (i1 < 0) {
            i1 -= amplitude >> 2;
        } else {
            i1 += amplitude >> 2;
        }

        if (i2 < 0) {
            i2 -= amplitude >> 2;
        } else {
            i2 += amplitude >> 2;
        }

        int count = durationInMillis / 50;
        if ((count & 1) == 0) {
            ++count;
        }

        ((Tween)animation.to(this, 2, accessor, 0.05F).targetRelative((float)i1, (float)i2).repeatAutoReverse(count, 0.0F)).ease(TweenEquations.Linear).start();
    }

    void setY(int y) {
        if (this.isDesktopNotification) {
            this.parent.setLocation(this.parent.getX(), y);
        } else {
            this.notifyCanvas.setLocation(this.notifyCanvas.getX(), y);
        }

    }

    int getY() {
        return this.isDesktopNotification ? this.parent.getY() : this.notifyCanvas.getY();
    }

    int getX() {
        return this.isDesktopNotification ? this.parent.getX() : this.notifyCanvas.getX();
    }

    void setLocation(int x, int y) {
        if (this.isDesktopNotification) {
            this.parent.setLocation(x, y);
        } else {
            this.notifyCanvas.setLocation(x, y);
        }

    }

    private static int getAnchorX(Pos position, Rectangle bounds, boolean isDesktop) {
        int startX;
        if (isDesktop) {
            startX = (int)bounds.getX();
        } else {
            startX = 0;
        }

        int screenWidth = (int)bounds.getWidth();
        switch(position) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return 20 + startX;
            case CENTER:
                return startX + screenWidth / 2 - 150 - 10;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return startX + screenWidth - 300 - 20;
            default:
                throw new RuntimeException("Unknown position. '" + position + "'");
        }
    }

    private static int getAnchorY(Pos position, Rectangle bounds, boolean isDesktop) {
        int startY;
        if (isDesktop) {
            startY = (int)bounds.getY();
        } else {
            startY = 0;
        }

        int screenHeight = (int)bounds.getHeight();
        switch(position) {
            case TOP_LEFT:
            case TOP_RIGHT:
                return startY + 20;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                if (isDesktop) {
                    return startY + screenHeight - 87 - 20;
                }

                return startY + screenHeight - 87 - 20 - 20;
            case CENTER:
                return startY + screenHeight / 2 - 43 - 10 - 10;
            default:
                throw new RuntimeException("Unknown position. '" + position + "'");
        }
    }

    private static void addPopupToMap(final LookAndFeel sourceLook) {
        synchronized(popups) {
            String id = sourceLook.idAndPosition;
            PopupList looks = (PopupList)popups.get(id);
            if (looks == null) {
                looks = new PopupList();
                popups.put(id, looks);
            }

            int index = looks.size();
            sourceLook.popupIndex = index;
            int anchorX = sourceLook.anchorX;
            int anchorY = sourceLook.anchorY;
            int targetY;
            if (index == 0) {
                targetY = anchorY;
            } else {
                boolean showFromTop = isShowFromTop(sourceLook);
                if (sourceLook.isDesktopNotification && index == 1) {
                    looks.calculateOffset(showFromTop, anchorX, anchorY);
                }

                if (showFromTop) {
                    targetY = anchorY + index * 97 + looks.getOffsetY();
                } else {
                    targetY = anchorY - index * 97 + looks.getOffsetY();
                }
            }

            looks.add(sourceLook);
            sourceLook.setLocation(anchorX, targetY);
            if (sourceLook.hideAfterDurationInSeconds > 0.0F && sourceLook.hideTween == null) {
                ((Tween)animation.to(sourceLook, 3, accessor, sourceLook.hideAfterDurationInSeconds).target(300.0F).ease(TweenEquations.Linear).addCallback(new TweenCallback() {
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == 8) {
                            sourceLook.notify.close();
                        }

                    }
                })).start();
            }

        }
    }

    private static boolean removePopupFromMap(LookAndFeel sourceLook) {
        boolean showFromTop = isShowFromTop(sourceLook);
        synchronized(popups) {
            boolean popupsAreEmpty = popups.isEmpty();
            PopupList allLooks = (PopupList)popups.get(sourceLook.idAndPosition);
            boolean adjustPopupPosition = false;
            Iterator iterator = allLooks.iterator();

            while(iterator.hasNext()) {
                LookAndFeel look = (LookAndFeel)iterator.next();
                if (look.tween != null) {
                    look.tween.cancel();
                    look.tween = null;
                }

                if (look == sourceLook) {
                    if (look.hideTween != null) {
                        look.hideTween.cancel();
                        look.hideTween = null;
                    }

                    adjustPopupPosition = true;
                    iterator.remove();
                }

                if (adjustPopupPosition) {
                    --look.popupIndex;
                }
            }

            int offsetY = allLooks.getOffsetY();

            for(int index = 0; index < allLooks.size(); ++index) {
                final LookAndFeel look = allLooks.get(index);
                int changedY;
                if (showFromTop) {
                    changedY = look.anchorY + look.popupIndex * 97 + offsetY;
                } else {
                    changedY = look.anchorY - (look.popupIndex * 97 + offsetY);
                }

                look.tween = (Tween)((Tween)animation.to(look, 1, accessor, MOVE_DURATION).target((float)changedY).ease(TweenEquations.Linear).addCallback(new TweenCallback() {
                    public void onEvent(int type, BaseTween<?> source) {
                        if (type == 8) {
                            look.tween = null;
                        }

                    }
                })).start();
            }

            return popupsAreEmpty;
        }
    }

    private static boolean isShowFromTop(LookAndFeel look) {
        switch(look.position) {
            case TOP_LEFT:
            case CENTER:
            case TOP_RIGHT:
                return true;
            case BOTTOM_LEFT:
            default:
                return false;
        }
    }

    void setProgress(int progress) {
        this.notifyCanvas.setProgress(progress);
    }

    int getProgress() {
        return this.notifyCanvas.getProgress();
    }

    void updatePositionsPre(boolean visible) {
        if (!visible) {
            boolean popupsAreEmpty = removePopupFromMap(this);
            SwingActiveRender.removeActiveRender(this.notifyCanvas);
            if (popupsAreEmpty) {
                SwingActiveRender.removeActiveRenderFrameStart(frameStartHandler);
            }
        }

    }

    void updatePositionsPost(boolean visible) {
        if (visible) {
            SwingActiveRender.addActiveRender(this.notifyCanvas);
            if (!SwingActiveRender.containsActiveRenderFrameStart(frameStartHandler)) {
                animation.resetUpdateTime();
                SwingActiveRender.addActiveRenderFrameStart(frameStartHandler);
            }

            addPopupToMap(this);
        }

    }

    static {
        MOVE_DURATION = Notification.MOVE_DURATION;
    }
}

