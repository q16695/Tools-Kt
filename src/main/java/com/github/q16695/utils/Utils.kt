package com.github.q16695.utils

import com.github.q16695.HttpRequest
import com.github.q16695.InputCommand
import com.github.q16695.Main
import com.github.q16695.Setting
import java.awt.Label
import java.io.File
import java.net.URL
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

fun String.toBoolean() : Boolean {
    return this.contains("true")
}

fun InputCommand.subArguments(index: Int) : String {
    var string = ""
    if(hasArguments()) {
        string = this.arguments.get(index)
        for(s in this.arguments.subList(index + 1, this.arguments.size)) {
            string = "$string $s"
        }
    }
    return string
}

fun URL.isValid() : Boolean {
    if(HttpRequest.get(this).body() == null) return false
    return true
}