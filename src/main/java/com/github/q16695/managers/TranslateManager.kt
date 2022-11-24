package com.github.q16695.managers

import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.TickEvent
import com.github.q16695.utils.Key

class TranslateManager {
    @EventHandler
    fun onTickEvent(e: TickEvent?) {
        translate = Main.Instance!!.translate
    }

    companion object {
        private var translate: Map<String, String?> = HashMap()
        fun setTranslate(value: Map<String, String?>) {
            translate = value
        }

        fun getTranslate(key: Key): String? {
            var string = key.string
            if (!string.contains(".")) {
                string = ".$string"
            }
            if (translate[string] != null) {
                return translate[string]
            } else if (translate[key.clazz.name + string] != null) {
                return translate[key.clazz.name + string]
            }
            return null
        }

        fun getTranslate(key: String): String? {
            return if (translate[key] != null) {
                translate[key]
            } else null
        }
    }

    init {
        EventManager.register(this)
    }
}
