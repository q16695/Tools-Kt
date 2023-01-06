package com.github.q16695.events

import com.github.q16695.managers.EventManager

open class Event {
    var cancel = false
    fun post() {
        EventManager.post(this)
    }

    enum class Stage {
        Pre, Post
    }
}
