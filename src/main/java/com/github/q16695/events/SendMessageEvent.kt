package com.github.q16695.events

import com.github.q16695.utils.MessageUtils

sealed class SendMessageEvent {
    class Pre(val string: String, val obj : Any , val type: MessageUtils.Type) : Event()
    class Post(val string: String, val obj : Any, val type: MessageUtils.Type) : Event()
}