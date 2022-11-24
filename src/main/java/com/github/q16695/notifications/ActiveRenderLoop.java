package com.github.q16695.notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.List;

/**
 * Loop that controls the active rendering process
 */
public
class ActiveRenderLoop implements Runnable {

    @Property
    /**
     * How many frames per second we want the Swing ActiveRender thread to run at
     *
     * NOTE: The ActiveRenderLoop replaces the Swing EDT (only for specified JFrames) in order to enable smoother animations. It is also
     *       important to REMEMBER -- if you add a component to an actively managed JFrame, YOU MUST make sure to call
     *       {@link JComponent#setIgnoreRepaint(boolean)} otherwise this component will "fight" on the EDT for updates. You can completely
     *       disable the EDT by calling {@link NullRepaintManager#install()}
     */
    public static int TARGET_FPS = 30;

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    @Override
    public
    void run() {
        long lastTime = System.nanoTime();

        // 30 FPS is usually just fine. This isn't a game where we need 60+ FPS. We permit this to be changed though, just in case it is.
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        Graphics graphics = null;

        while (SwingActiveRender.hasActiveRenders) {
            long now = System.nanoTime();
            long updateDeltaNanos = now - lastTime;
            lastTime = now;

            // not synchronized, because we don't care. The worst case, is one frame of animation behind.
            for (int i = 0; i < SwingActiveRender.activeRenderEvents.size(); i++) {
                ActionHandlerLong actionHandlerLong = SwingActiveRender.activeRenderEvents.get(i);

                //noinspection unchecked
                actionHandlerLong.handle(updateDeltaNanos);
            }

            // this needs to be synchronized because we don't want to our canvas removed WHILE we are rendering it.
            synchronized (SwingActiveRender.activeRenders) {
                final List<Canvas> activeRenders = SwingActiveRender.activeRenders;

                for (Canvas canvas : activeRenders) {
                    if (!canvas.isDisplayable()) {
                        continue;
                    }

                    BufferStrategy buffer = canvas.getBufferStrategy();

                    // maybe the frame was closed
                    try {
                        graphics = buffer.getDrawGraphics();
                        canvas.paint(graphics);
                    } catch (Exception e) {
                        // the frame can be close as well. can get a "java.lang.IllegalStateException: Component must have a valid
                        // peer" if it's already be closed during the getDrawGraphics call.
                        e.printStackTrace();
                    } finally {
                        if (graphics != null) {
                            graphics.dispose();

                            // blit the back buffer to the screen
                            if (buffer != null && !buffer.contentsLost()) {
                                buffer.show();
                            }
                        }
                    }
                }
            }

            // Sync the display on some systems (on Linux, this fixes event queue problems)
            Toolkit.getDefaultToolkit()
                    .sync();

            try {
                // Converted to int before the division, because IDIV is
                // 1 order magnitude faster than LDIV (and int's work for us anyways)
                // see: http://www.cs.nuim.ie/~jpower/Research/Papers/2008/lambert-qapl08.pdf
                // Also, down-casting (long -> int) is not expensive w.r.t IDIV/LDIV
                //noinspection NumericCastThatLosesPrecision
                final int l = (int) (lastTime - System.nanoTime() + OPTIMAL_TIME);
                final int millis = l / 1000000;
                if (millis > 1) {
                    Thread.sleep(millis);
                }
                else {
                    // try to keep the CPU from getting slammed. We couldn't match our target FPS, so loop again
                    Thread.yield();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}

