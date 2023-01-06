package com.github.q16695.events

import com.github.q16695.utils.Vec2d

class MouseEvent(val keyCode: Int, val pressed: Boolean, val location : Vec2d) : Event() {
    fun isDownMouse(key: Int): Boolean {
        return keyCode == key && pressed
    }

    fun isKey(key: Int): Boolean {
        return keyCode == key
    }

    override fun toString(): String {
        return "MouseEvent{" +
                "keyCode=" + keyCode +
                ", pressed=" + pressed +
                '}'
    }

    companion object {
        const val RIGHT = 3
        const val LEFT = 1
        const val MIDDLE = 2
    }
}
