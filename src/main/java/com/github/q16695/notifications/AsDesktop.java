package com.github.q16695.notifications;

import javax.swing.*;
import java.awt.*;

public class AsDesktop extends JWindow implements INotify {
    private static final long serialVersionUID = 1L;
    private final LookAndFeel look;
    private final Notification notification;

    AsDesktop(Notification notification, ImageIcon image, Theme theme) {
        this.notification = notification;
        this.setAlwaysOnTop(true);
        Dimension preferredSize = new Dimension(1, 2);
        this.setPreferredSize(preferredSize);
        this.setMaximumSize(preferredSize);
        this.setMinimumSize(preferredSize);
        this.setSize(300, 87);
        this.setLocation(-32768, -32768);
        GraphicsDevice device;
        if (notification.screenNumber == -32768) {
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            device = ScreenUtil.getMonitorAtLocation(mouseLocation);
        } else {
            int screenNumber = notification.screenNumber;
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] screenDevices = ge.getScreenDevices();
            if (screenNumber < 0) {
                screenNumber = 0;
            } else if (screenNumber > screenDevices.length - 1) {
                screenNumber = screenDevices.length - 1;
            }

            device = screenDevices[screenNumber];
        }

        Rectangle bounds = device.getDefaultConfiguration().getBounds();
        NotifyCanvas notifyCanvas = new NotifyCanvas(this, notification, image, theme);
        this.getContentPane().add(notifyCanvas);
        this.look = new LookAndFeel(this, this, notifyCanvas, notification, bounds, true);
    }

    public void onClick(int x, int y) {
        this.look.onClick(x, y);
    }

    public void shake(int durationInMillis, int amplitude) {
        this.look.shake(durationInMillis, amplitude);
    }

    public void setVisible(boolean visible) {
        if (visible != this.isVisible()) {
            this.look.updatePositionsPre(visible);
            super.setVisible(visible);
            this.look.updatePositionsPost(visible);
            if (visible) {
                this.toFront();
            }

        }
    }

    void doHide() {
        super.setVisible(false);
    }

    public void close() {
        SwingUtil.invokeLater(new Runnable() {
            public void run() {
                AsDesktop.this.doHide();
                AsDesktop.this.look.close();
                AsDesktop.this.removeAll();
                AsDesktop.this.dispose();
                AsDesktop.this.notification.onClose();
            }
        });
    }
}
