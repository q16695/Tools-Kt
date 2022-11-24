package com.github.q16695.notifications;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contains all of the appropriate logic to setup and render via "Active" rendering (instead of "Passive" rendering). This permits us to
 * render Windows (and their contents), OFF of the EDT - even though there are other frames/components that are ON the EDT. <br> Because we
 * still want to react to mouse events, etc on the EDT, we do not completely remove the EDT -- we merely allow us to "synchronize" the EDT
 * object to our thread. It's a little bit hacky, but it works beautifully, and permits MUCH nicer animations. <br>
 * <p/>
 * <b>It is also important to REMEMBER -- if you add a component to an actively managed Window, YOU MUST make sure to call {@link
 * JComponent#setIgnoreRepaint(boolean)} otherwise this component will "fight" on the EDT for updates. </b>
 */
public final
class SwingActiveRender {
    private static Thread activeRenderThread = null;

    static final java.util.List<Canvas> activeRenders = new ArrayList<Canvas>();
    static final List<ActionHandlerLong> activeRenderEvents = new CopyOnWriteArrayList<ActionHandlerLong>();

    // volatile, so that access triggers thread synchrony, since 1.6. See the Java Language Spec, Chapter 17
    static volatile boolean hasActiveRenders = false;

    private static final Runnable renderLoop = new ActiveRenderLoop();

    private
    SwingActiveRender() {
    }


    /**
     * Enables the canvas to to added to an "Active Render" thread, at a target "Frames-per-second". This is to support smooth, swing-based
     * animations.
     * <p>
     * This works by removing this object from EDT updates, and instead manually calls paint(g) on the canvas, updating it on our own thread.
     *
     * @param canvas the canvas to add to the ActiveRender thread.
     */
    @SuppressWarnings("Duplicates")
    public static
    void addActiveRender(final Canvas canvas) {
        // this should be on the EDT
        if (!EventQueue.isDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public
                void run() {
                    addActiveRender(canvas);
                }
            });
            return;
        }

        // setup double-buffering, so we can properly use Active-Rendering, so the animations will be smooth
        try {
            canvas.createBufferStrategy(2);
        } catch (Exception e) {
            // sometimes it's added too early. Postpone the event until later
            // note: this is different than SwingUtil, because we MUST invoke it later (and not in the current thread)
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public
                void run() {
                    addActiveRender(canvas);
                }
            });
            return;
        }

        canvas.setIgnoreRepaint(true);

        synchronized (activeRenders) {
            if (!hasActiveRenders) {
                setupActiveRenderThread();
            }

            hasActiveRenders = true;
            activeRenders.add(canvas);
        }
    }

    /**
     * Removes a canvas from the ActiveRender queue. This should happen when the canvas is closed.
     *
     * @param canvas the canvas to remove
     */
    public static
    void removeActiveRender(final Canvas canvas) {
        // this should be on the EDT
        if (!EventQueue.isDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public
                void run() {
                    removeActiveRender(canvas);
                }
            });
            return;
        }

        synchronized (activeRenders) {
            activeRenders.remove(canvas);

            final boolean hadActiveRenders = !activeRenders.isEmpty();
            hasActiveRenders = hadActiveRenders;

            if (!hadActiveRenders) {
                activeRenderThread = null;
            }
        }

        canvas.setIgnoreRepaint(false);
    }

    /**
     * Specifies an ActionHandler to be called when the ActiveRender thread starts to render at each tick.
     *
     * @param handler the handler to add
     */
    public static
    void addActiveRenderFrameStart(final ActionHandlerLong handler) {
        synchronized (activeRenders) {
            activeRenderEvents.add(handler);
        }
    }

    /**
     * Potentially SLOW calculation, as it compares each entry in a queue for equality
     *
     * @param handler this is the handler to check
     *
     * @return true if this handler already exists in the active render, on-frame-start queue
     */
    public static
    boolean containsActiveRenderFrameStart(final ActionHandlerLong handler) {
        synchronized (activeRenders) {
            return activeRenderEvents.contains(handler);
        }
    }

    /**
     * Removes the handler from the on-frame-start queue
     *
     * @param handler the handler to remove
     */
    public static
    void removeActiveRenderFrameStart(final ActionHandlerLong handler) {
        synchronized (activeRenders) {
            activeRenderEvents.remove(handler);
        }
    }

    /**
     * Creates (if necessary) the active-render thread. When there are no active-render targets, this thread will exit
     */
    private static
    void setupActiveRenderThread() {
        if (activeRenderThread != null) {
            return;
        }

        SynchronizedEventQueue.install();

        activeRenderThread = new Thread(renderLoop, "AWT-ActiveRender");
        activeRenderThread.setDaemon(true);
        activeRenderThread.start();
    }
}

