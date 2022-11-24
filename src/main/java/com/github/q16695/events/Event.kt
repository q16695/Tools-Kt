package com.github.q16695.events

import com.github.q16695.Main

open class Event {
    fun post() {
        Main.Instance!!.eventManager.post(this)
    }

    enum class Stage {
        Pre, Post
    }
}
