package com.github.q16695.miscs

import javax.swing.Action
import javax.swing.Icon
import javax.swing.JButton

class JButton(text : String?, icon : Icon?) : JButton() {
    constructor(text: String) : this (text, null)
    constructor(icon : Icon) : this (null,icon)
    constructor() : this(null,null)
    constructor(a : Action) : this() {
        action = a;
    }
}