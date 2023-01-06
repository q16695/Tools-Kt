package com.github.q16695.modules

import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.MouseEvent
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.Key
import java.awt.Color
import java.awt.MouseInfo
import java.awt.event.MouseListener
import javax.swing.JButton

class RightModule : EmptyModule(true) {
    @EventHandler
    fun onKeyEvent(event: MouseEvent) {
        if (event.isDownMouse(MouseEvent.LEFT)) {
            for (J in visibleButtons) {
                Main.Instance!!.frame.remove(J)
            }
            visibleButtons.clear()
        } else if (event.isDownMouse(MouseEvent.RIGHT)) {
            for (J in visibleButtons) {
                Main.Instance!!.frame.remove(J)
            }
            visibleButtons.clear()
            val up = JButton(TranslateManager.getTranslate("${this.javaClass.name}.exit"))
            up.foreground = Color.RED
            up.isOpaque = true
            up.background = Color.WHITE
            up.font = Main.Instance!!.textFont
            up.setBounds(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y, 100, 25)
            Main.Instance!!.frame.add(up)
            visibleButtons.add(up)
            up.addMouseListener(object : MouseListener {
                override fun mouseClicked(e: java.awt.event.MouseEvent) {}
                override fun mousePressed(e: java.awt.event.MouseEvent) {
                    Main.Instance!!.frame.isVisible = false
                }
                override fun mouseReleased(e: java.awt.event.MouseEvent) {}
                override fun mouseEntered(e: java.awt.event.MouseEvent) {}
                override fun mouseExited(e: java.awt.event.MouseEvent) {}
            })
        }
    }

    @EventHandler
    fun onTickEvent() {
        if (Main.Instance!!.selectJLabel == null) {
            for (c in Main.Instance!!.helpList) {
                Main.Instance!!.frame.remove(c)
            }
            Main.Instance!!.helpList.clear()
        }
        if (Main.Instance!!.selectJLabel != null && Main.Instance!!.command.text == Main.Instance!!.selectJLabel!!.text) {
            if (!Main.Instance!!.helpList.isEmpty()) {
                for (s in Main.Instance!!.helpList) {
                    Main.Instance!!.frame.remove(s)
                }
                Main.Instance!!.selectJLabel = null
                Main.Instance!!.helpList.clear()
            }
        }
    }

    companion object {
        var visibleButtons = ArrayList<JButton>()
    }
}
