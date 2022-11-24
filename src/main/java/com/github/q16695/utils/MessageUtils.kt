package com.github.q16695.utils

import com.github.q16695.Config
import com.github.q16695.Main
import com.github.q16695.notifications.Notification
import javazoom.jl.player.Player
import java.io.File
import java.util.*
import javax.swing.JLabel

object MessageUtils {
    fun send(strings: List<String?>) {
        synchronized(Main::class.java) {
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
                    s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.tip.y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                } else {
                    s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.extra[i - 1].y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                }
                s.foreground = Main.Instance!!.color
                s.font = Main.Instance!!.textFont
                Main.Instance!!.extra.add(s)
                i++
            }
            Thread {
                val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/information.mp3")))
                player.play()
            }.start()
        }
    }

    fun send(strings: ArrayList<String>) {
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
                s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.tip.y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
            } else {
                s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.extra[i - 1].y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
            }
            s.foreground = Main.Instance!!.color
            s.font = Main.Instance!!.textFont
            Main.Instance!!.extra.add(s)
            i++
        }
        Thread {
            val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/information.mp3")))
            player.play()
        }.start()
    }

    fun sendError(strings: List<String>) {
        synchronized(Main::class.java) {
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
                    s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.tip.y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                } else {
                    s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.extra[i - 1].y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                }
                s.foreground = Main.Instance!!.color
                s.font = Main.Instance!!.textFont
                Main.Instance!!.extra.add(s)
                i++
            }
            Thread {
                val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/error.mp3")))
                player.play()
            }.start()
        }
    }

    fun sendError(strings: ArrayList<String>) {
        synchronized(Main::class.java) {
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
                    s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.tip.y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                } else {
                    s.setBounds(Main.Instance!!.tip.x, Main.Instance!!.extra[i - 1].y + 25, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                }
                s.foreground = Main.Instance!!.color
                s.font = Main.Instance!!.textFont
                Main.Instance!!.extra.add(s)
                i++
            }
            Thread {
                val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/error.mp3")))
                player.play()
            }.start()
        }
    }

    fun sendError(string: String?) {
        Thread {
            if (!Main.notification) {
                Main.Instance!!.tip.text = string
                for (c in Main.Instance!!.extra) {
                    Main.Instance!!.frame.remove(c)
                }
                Main.Instance!!.extra.clear()
                //System.out.println(string);
            } else {
                Notification.create()
                    .title(Config(File(" ").canonicalPath + "config").getValue("MainName"))
                    .text(string)
                    .hideAfter(2000)
                    .showError()
            }
            val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/error.mp3")))
            player.play()
        }.start()
    }

    fun sendErrors(string: String?) {
        Main.Instance!!.tip.text = string
        for (c in Main.Instance!!.extra) {
            Main.Instance!!.frame.remove(c)
        }
        Main.Instance!!.extra.clear()
        Thread {
            val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/error.mp3")))
            player.play()
        }.start()
    }

    fun send(string: String) {
        Thread {
            if (!Main.notification) {
                Main.Instance!!.tip.text = string
                for (c in Main.Instance!!.extra) {
                    Main.Instance!!.frame.remove(c)
                }
                Main.Instance!!.extra.clear()
                //System.out.println(string);
            } else {
                Notification.create()
                    .title(Main.Instance!!.frame.title)
                    .text(string)
                    .hideCloseButton()
                    .hideAfter(2000)
                    .showInformation()
            }
            val player = Player(this.javaClass.getResourceAsStream("/assets/information.mp3"))
            player.play()
        }.start()
    }

    fun sendInformation(string: String?) {
        Main.Instance!!.tip.text = string
        for (c in Main.Instance!!.extra) {
            Main.Instance!!.frame.remove(c)
        }
        Main.Instance!!.extra.clear()
        Thread {
            val player = Player(Objects.requireNonNull(Main::class.java.getResourceAsStream("/assets/error.mp3")))
            player.play()
        }.start()
        //System.out.println(string);
    }

    fun send(string: String?, length: Int) {
        Thread {
            if (!Main.notification) {
                Main.Instance!!.tip.text = string
                for (c in Main.Instance!!.extra) {
                    Main.Instance!!.frame.remove(c)
                }
                Main.Instance!!.extra.clear()
                //System.out.println(string);
            } else {
                Notification.create()
                    .title(Config(File(" ").canonicalPath + "config").getValue("MainName")!!)
                    .text(string)
                    .hideCloseButton()
                    .hideAfter(length * 1000)
                    .showInformation()
            }
            val player = Player(this.javaClass.getResourceAsStream("/assets/information.mp3"))
            player.play()
        }.start()
    }

    fun sendMessage(string: String?) {
        println(string)
    }

    fun sendNotification(string: String) {
        Thread {
            Notification.create()
                .title(Config(File(" ").canonicalPath + "config").getValue("MainName")!!)
                .text(string)
                .hideCloseButton()
                .hideAfter(2000)
                .showInformation()
            val player = Player(this.javaClass.getResourceAsStream("/assets/information.mp3"))
            player.play()
        }.start()
    }
}
