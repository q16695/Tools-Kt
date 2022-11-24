package com.github.q16695.notifications;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ClickAdapter extends MouseAdapter {

    ClickAdapter() {
    }

    @Override
    public
    void mouseReleased(final MouseEvent e) {
        INotify parent = ((NotifyCanvas) e.getSource()).parent;
        parent.onClick(e.getX(), e.getY());
    }
}

