package com.github.q16695.managers

import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.utils.Key

object TranslateManager {
    @EventHandler
    fun onTickEvent() {
        translate = Main.Instance!!.translate
    }

    private var translate: Map<String, String?> = HashMap()
    fun setTranslate(value: Map<String, String?>) {
        translate = value
    }

    fun getTranslate(key: String): String? {
        return if (translate[key] != null) {
            translate[key]
        } else null
    }

    init {
        EventManager.register(this)
    }
}
