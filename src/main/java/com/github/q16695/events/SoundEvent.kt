package com.github.q16695.events

import com.github.q16695.utils.Sound

sealed class SoundEvent {
    class Pre(val sound : Sound) : Event()
    class Post(val sound : Sound) : Event()
}