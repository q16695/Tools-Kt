package com.github.q16695.events

import com.github.q16695.miscs.JFrame

sealed class ButtonEvent {
    class Add(val button : javax.swing.JButton, val frame : JFrame) : Event()
    class Remove(val button : javax.swing.JButton, val frame : JFrame) : Event()
}