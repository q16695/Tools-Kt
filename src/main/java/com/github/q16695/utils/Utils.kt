package com.github.q16695.utils

import com.github.q16695.Main
import com.github.q16695.Setting
import java.awt.Label
import java.io.File
import javax.swing.JLayeredPane

fun Setting.setValue(value: String) {
    Main.config.setValue(key, value)
}

fun JLayeredPane.contains(label: Label) : Boolean {
    return this.getIndexOf(label) != -1
}

fun File.isFolder() : Boolean {
    return this.exists() && this.isDirectory
}

fun File.isFile() : Boolean {
    return this.exists() && this.isFile
}