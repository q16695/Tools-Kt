package com.github.q16695.modules

import com.github.q16695.events.Event
import com.github.q16695.managers.EventManager


open class EmptyModule(hasListener: Boolean) {
    open fun onEvent(event: Event) {}

    init {
        if (hasListener) {
            EventManager.register(this)
        }
    }
}
