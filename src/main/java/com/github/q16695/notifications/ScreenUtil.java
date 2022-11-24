package com.github.q16695.notifications;

import java.awt.*;

public final class ScreenUtil {
    public static Rectangle getScreenBoundsAt(Point pos) {
        GraphicsDevice gd = getMonitorAtLocation(pos);
        Rectangle bounds = null;
        if (gd != null) {
            bounds = gd.getDefaultConfiguration().getBounds();
        }

        return bounds;
    }

    public static GraphicsDevice getMonitorAtMouseLocation() {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        return getMonitorAtLocation(mouseLocation);
    }

    public static GraphicsDevice getMonitorAtLocation(Point pos) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = ge.getScreenDevices();
        GraphicsDevice device = null;
        GraphicsDevice[] var4 = screenDevices;
        int var5 = screenDevices.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            GraphicsDevice device1 = var4[var6];
            GraphicsConfiguration gc = device1.getDefaultConfiguration();
            Rectangle screenBounds = gc.getBounds();
            if (screenBounds.contains(pos)) {
                device = device1;
                break;
            }
        }

        if (device == null) {
            device = ge.getDefaultScreenDevice();
        }

        return device;
    }

    public static int getMonitorNumberAtMouseLocation() {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        return getMonitorNumberAtLocation(mouseLocation);
    }

    public static int getMonitorNumberAtLocation(Point pos) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screenDevices = ge.getScreenDevices();

        for(int i = 0; i < screenDevices.length; ++i) {
            GraphicsDevice device1 = screenDevices[i];
            GraphicsConfiguration gc = device1.getDefaultConfiguration();
            Rectangle screenBounds = gc.getBounds();
            if (screenBounds.contains(pos)) {
                return i;
            }
        }

        return 0;
    }

    public static void showOnSameScreenAsMouse_Center(Container frame) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        GraphicsDevice monitorAtMouse = getMonitorAtLocation(mouseLocation);
        Rectangle bounds = monitorAtMouse.getDefaultConfiguration().getBounds();
        frame.setLocation(bounds.x + bounds.width / 2 - frame.getWidth() / 2, bounds.y + bounds.height / 2 - frame.getHeight() / 2);
    }

    public static void showOnSameScreenAsMouse(Container frame) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        GraphicsDevice monitorAtMouse = getMonitorAtLocation(mouseLocation);
        Rectangle bounds = monitorAtMouse.getDefaultConfiguration().getBounds();
        frame.setLocation(bounds.x, bounds.y);
    }

    private ScreenUtil() {
    }
}

