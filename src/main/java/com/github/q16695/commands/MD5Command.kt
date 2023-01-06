package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.MD5Utils.MD5
import com.github.q16695.events.EventHandler
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils.sendError
import com.github.q16695.utils.MessageUtils.sendInformation

class MD5Command : BasicCommand("md5", "encryption the string") {
    var extra: String? = null
    @EventHandler
    fun onTickEvent() {
        if (TranslateManager.getTranslate("${this.javaClass.name}.description") != null) {
            description = TranslateManager.getTranslate("${this.javaClass.name}.description")!!
        }
    }

    override fun onInput(string: String?) {
        super.onInput(string)
        if (string!!.contains(" ")) {
            extra = string.substring(string.indexOf(" "))
        } else {
            extra = " "
        }
    }

    override fun onExecute() {
        if (extra != " ") {
            sendInformation(MD5(extra!!)!!,this)
        } else {
            sendError(TranslateManager.getTranslate("${this.javaClass.name}.error")!!,this)
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
