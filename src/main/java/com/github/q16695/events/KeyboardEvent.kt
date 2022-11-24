package com.github.q16695.events

import com.github.q16695.utils.KeyBind

class KeyboardEvent(keyCode: Int, pressed: Boolean) : Event() {
    var keyBind: KeyBind

    init {
        keyBind = KeyBind(keyCode, pressed)
    }
}
