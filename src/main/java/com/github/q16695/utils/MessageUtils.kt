package com.github.q16695.utils

import com.github.q16695.Config
import com.github.q16695.Main
import com.github.q16695.events.SendMessageEvent
import com.github.q16695.notifications.Notification
import java.io.File
import java.util.*
import javax.swing.JLabel

object MessageUtils {

    fun send(strings: List<String>, obj : Any) {
        send(strings = strings, type = Type.INFORMATION, obj = obj)
    }

    fun sendError(strings: List<String>, obj: Any) {
        send(strings = strings, type = Type.ERROR, obj = obj)
    }

    fun sendError(strings: ArrayList<String>, obj: Any) {
        send(strings = strings, type = Type.ERROR, obj = obj)
    }

    fun sendError(string: String, obj: Any) {
        send(string = string, length = 2000, notification = Main.notification, type = Type.ERROR, obj = obj)
    }

    fun sendErrorMessage(string: String, obj: Any) {
        send(string = string, length = 0, notification = false, type = Type.ERROR, obj = obj)
    }

    fun send(string: String, obj: Any) {
        send(string = string, length = 2000, notification = Main.notification, type = Type.INFORMATION, obj = obj)
    }

    fun sendInformation(string: String, obj: Any) {
        send(string = string, length = 0, notification = false, type = Type.INFORMATION, obj = obj)
    }
    fun send(string: String, length: Int, obj : Any) {
        send(string = string,length = length * 1000, notification = true, type = Type.INFORMATION, obj = obj)
    }

    fun sendNotification(string: String, obj: Any) {
        send(string = string, length = 2000, notification = true, type = Type.INFORMATION, obj = obj)
    }

    fun send(strings: List<String>, type : Type, obj: Any) {
        Thread {
            for(string in strings) {
                SendMessageEvent.Pre(string = string, obj = obj, type = type).post()
                Main.Instance!!.tip.text = strings[0]
                for (c in Main.Instance!!.extra) {
                    Main.Instance!!.frame.remove(c)
                }
                Main.Instance!!.extra.clear()
                var i = 0
                for (e in strings) {
                    if (strings.indexOf(e) == 0) continue
                    val s = JLabel(e)
                    if (Main.Instance!!.extra.size == 0) {
                        s.setBounds(
                            Main.Instance!!.tip.x,
                            Main.Instance!!.tip.y + 25,
                            Main.Instance!!.tip.width,
                            Main.Instance!!.tip.height
                        )
                    } else {
                        s.setBounds(
                            Main.Instance!!.tip.x,
                            Main.Instance!!.extra[i - 1].y + 25,
                            Main.Instance!!.tip.width,
                            Main.Instance!!.tip.height
                        )
                    }
                    s.foreground = Main.Instance!!.color
                    s.font = Main.Instance!!.textFont
                    Main.Instance!!.extra.add(s)
                    i++
                }
                SendMessageEvent.Post(string = string, obj = obj, type = type).post()
            }
            SoundUtil.playSoundByResource("/assets/${type.value}.mp3",false,type.value)
        }.start()
    }

    private fun send(string: String, length: Int, notification: Boolean, type : Type, obj : Any) {
        SendMessageEvent.Pre(string, obj, type).post()
        Thread {
            if (!notification && Main.ready) {
                Main.Instance!!.tip.text = string
                for (c in Main.Instance!!.extra) {
                    Main.Instance!!.frame.remove(c)
                }
                Main.Instance!!.extra.clear()
            } else {
                if (length != 0) {
                    Notification.create()
                        .title(Config(fileName = File(" ").canonicalPath + "config").getValue(key = "MainName")!!)
                        .text(string)
                        .hideCloseButton()
                        .hideAfter(length)
                        .showInformation()
                } else {
                    Notification.create()
                        .title(Config(fileName = File(" ").canonicalPath + "config").getValue(key = "MainName")!!)
                        .text(string)
                        .showInformation()
                }
            }
            SoundUtil.playSoundByResource("/assets/${type.value}.mp3", false, type.value)
        }.start()
        SendMessageEvent.Post(string, obj, type).post()
    }

    enum class Type(val value: String) {
        INFORMATION("information"),
        ERROR("error"),
        WARNING("warning")
    }
}
