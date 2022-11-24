package com.github.q16695.notifications;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

class PopupList {
    private int offsetY = 0;
    private ArrayList<LookAndFeel> popups = new ArrayList(4);

    PopupList() {
    }

    void calculateOffset(boolean showFromTop, int anchorX, int anchorY) {
        if (this.offsetY == 0) {
            Point point = new Point(anchorX, anchorY);
            GraphicsConfiguration gc = ScreenUtil.getMonitorAtLocation(point).getDefaultConfiguration();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            if (showFromTop) {
                if (screenInsets.top > 0) {
                    this.offsetY = screenInsets.top - 20;
                }
            } else if (screenInsets.bottom > 0) {
                this.offsetY = screenInsets.bottom + 20;
            }
        }

    }

    int getOffsetY() {
        return this.offsetY;
    }

    int size() {
        return this.popups.size();
    }

    void add(LookAndFeel lookAndFeel) {
        this.popups.add(lookAndFeel);
    }

    Iterator<LookAndFeel> iterator() {
        return this.popups.iterator();
    }

    LookAndFeel get(int index) {
        return this.popups.get(index);
    }
}

