package com.github.q16695.managers

import java.awt.Label
import java.awt.MouseInfo
import javax.swing.JButton
import javax.swing.JTextField

object MouseManager {
    fun inLabel(label: Label): Boolean {
        return label.contains(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y)
    }

    fun inLabel(label: JTextField): Boolean {
        return label.contains(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y)
    }

    fun inButton(button: JButton): Boolean {
        return button.contains(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y)
    }

    fun inButtons(buttons: ArrayList<JButton>): Boolean {
        var re = false
        for (button in buttons) {
            if (button.contains(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y)) {
                re = true
            }
        }
        return re
    }
}