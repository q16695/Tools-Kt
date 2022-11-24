package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.TickEvent
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.ImageUtil.toBufferedImage
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils
import com.github.q16695.utils.MessageUtils.sendErrors
import com.github.q16695.utils.MessageUtils.sendNotification
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class ImageCommand : BasicCommand("image", "save the image") {

    @EventHandler
    fun onTickEvent() {
        if (TranslateManager.getTranslate(this.javaClass.name + ".description") != null) {
            description = TranslateManager.getTranslate(this.javaClass.name + ".description")!!
        }
    }

    override fun onExecute() {
        if (command!!.hasArguments()) {
            if (command!!.arguments[0].lowercase() == "save") {
                var i = 0
                while (true) {
                    if (!File(File(" ").canonicalPath + "image" + i + ".png").exists()) {
                        File(File(" ").canonicalPath + "image" + i + ".png").createNewFile()
                        break
                    }
                    i++
                }
                val file = File(File(" ").canonicalPath + "image" + i + ".png")
                val bufferedImage = toBufferedImage(Main.Instance!!.image!!)
                ImageIO.write(bufferedImage, "png", file)
                sendNotification(Objects.requireNonNull(TranslateManager.getTranslate(Key("successfulSave", this.javaClass)))!!.replace("%fileName%", File(" ").absolutePath + "image" + i + ".png"))
            } else if (command!!.arguments[0].lowercase() == "help") {
                MessageUtils.send(listOf(TranslateManager.getTranslate(Key("help1", this.javaClass)), TranslateManager.getTranslate(Key("help2", this.javaClass)), TranslateManager.getTranslate(Key("help3", this.javaClass))))
            } else if (command!!.arguments[0].lowercase() == "select") {
                if (command!!.arguments.size > 1) {
                    if (Main.Instance!!.background != null) {
                        val bg = ImageIcon(File(" ").canonicalPath + command!!.arguments[1])
                        Main.Instance!!.background!!.icon = bg
                        Main.Instance!!.background!!.setSize(bg.iconWidth, bg.iconHeight)
                    }
                    sendNotification(TranslateManager.getTranslate(Key("successfulSelect", this.javaClass))!!)
                } else {
                    MessageUtils.sendError(listOf(TranslateManager.getTranslate(Key("error1", this.javaClass))!!, TranslateManager.getTranslate(Key("error1.1", this.javaClass))!!))
                }
            } else if (command!!.arguments[0].lowercase() == "refresh") {
                if (Main.Instance!!.background != null) {
                    val bg = ImageIcon(URL("https://api.ghser.com/random/api.php"))
                    Main.Instance!!.background!!.icon = bg
                    Main.Instance!!.background!!.setSize(Objects.requireNonNull(bg).iconWidth, bg.iconHeight)
                }
            } else {
                sendErrors(TranslateManager.getTranslate(Key("error2", this.javaClass)))
            }
        } else {
            sendErrors(TranslateManager.getTranslate(Key("error2", this.javaClass)))
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
