package com.github.q16695.notifications;

import java.awt.event.WindowEvent;

class WindowAdapter extends java.awt.event.WindowAdapter {
    @Override
    public
    void windowClosing(WindowEvent e) {
        if (e.getNewState() != WindowEvent.WINDOW_CLOSED) {
            AsDesktop source = (AsDesktop) e.getSource();
            source.close();
        }
    }

    @Override
    public
    void windowLostFocus(WindowEvent e) {
        if (e.getNewState() != WindowEvent.WINDOW_CLOSED) {
            AsDesktop source = (AsDesktop) e.getSource();
            // these don't work
            //toFront();
            //requestFocus();
            //requestFocusInWindow();
            source.setAlwaysOnTop(false);
            source.setAlwaysOnTop(true);
        }
    }
}

