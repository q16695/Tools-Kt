package com.github.q16695.miscs

import com.github.q16695.events.ButtonEvent
import java.awt.Component
import java.awt.MenuComponent
import javax.swing.JButton
import javax.swing.JFrame

class JFrame : JFrame() {
    override fun remove(comp: Component?) {
        super.remove(comp)
        if (comp == rootPane) {
            super.remove(comp)
        } else {
            contentPane.remove(comp)
        }
        if(comp is JButton || comp is com.github.q16695.miscs.JButton) {
            ButtonEvent.Remove(comp as JButton, this).post()
        }
    }

    override fun add(comp: Component?, index: Int): Component? {
        addImpl(comp, null, index)
        if (comp != null) {
            if (comp is JButton || comp is com.github.q16695.miscs.JButton) {
                ButtonEvent.Add(comp as JButton, this).post()
            }
        }
        return comp
    }

    override fun add(name: String?, comp: Component?): Component? {
        addImpl(comp, name, -1)
        if(comp != null) {
            if(comp is JButton || comp is com.github.q16695.miscs.JButton) {
                ButtonEvent.Add(comp as JButton, this).post()
            }
        }
        return comp
    }

    override fun add(comp: Component?): Component {
        addImpl(comp, null, -1)
        if(comp != null) {
            if(comp is JButton || comp is com.github.q16695.miscs.JButton) {
                ButtonEvent.Add(comp as JButton, this).post()
            }
        }
        return super.add(comp)
    }
}