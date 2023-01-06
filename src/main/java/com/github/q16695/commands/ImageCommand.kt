package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.ImageUtil.toBufferedImage
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils
import com.github.q16695.utils.MessageUtils.sendError
import com.github.q16695.utils.MessageUtils.sendNotification
import com.github.q16695.utils.isFolder
import java.io.File
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
        val folder = File("${File(" ").canonicalPath}pics")
        if(!folder.exists()) {
            folder.mkdir()
        }
        if (command!!.hasArguments()) {
            if (command!!.arguments[0].lowercase() == "save") {
                var i = 0
                while (true) {
                    if (!File("${File(" ").canonicalPath}pics/image$i.png").exists()) {
                        File("${File(" ").canonicalPath}pics/image$i.png").createNewFile()
                        break
                    }
                    i++
                }
                val file = File("${File(" ").canonicalPath}pics/image$i.png")
                val bufferedImage = toBufferedImage(Main.Instance!!.image!!)
                ImageIO.write(bufferedImage, "png", file)
                sendNotification(TranslateManager.getTranslate("${this.javaClass.name}.successfulSave")!!.replace("%fileName%",  "${File(" ").canonicalPath}pics/image$i.png"), obj = this)
            } else if (command!!.arguments[0].lowercase() == "help") {
                MessageUtils.send(listOf(TranslateManager.getTranslate("${this.javaClass.name}.help1")!!, TranslateManager.getTranslate("${this.javaClass.name}.help2")!!, TranslateManager.getTranslate("${this.javaClass.name}.help3")!!), obj = this)
            } else if (command!!.arguments[0].lowercase() == "select") {
                if (command!!.arguments.size > 1) {
                    if (Main.Instance!!.background != null) {
                        val bg = ImageIcon(File(" ").canonicalPath + command!!.arguments[1])
                        Main.Instance!!.background!!.icon = bg
                        Main.Instance!!.background!!.setSize(bg.iconWidth, bg.iconHeight)
                    }
                    sendNotification(TranslateManager.getTranslate("${this.javaClass.name}.successfulSelect")!!, obj = this)
                } else {
                    sendError(listOf(TranslateManager.getTranslate("${this.javaClass.name}.error1")!!, TranslateManager.getTranslate("${this.javaClass.name}.error1.1")!!), obj = this)
                }
            } else if (command!!.arguments[0].lowercase() == "refresh") {
                if (Main.Instance!!.background != null) {
                    val bg = ImageIcon(URL("https://api.ghser.com/random/api.php"))
                    Main.Instance!!.background!!.icon = bg
                    Main.Instance!!.background!!.setSize(bg.iconWidth, bg.iconHeight)
                }
            } else {
                sendError(TranslateManager.getTranslate("${this.javaClass.name}.error2")!!, obj = this)
            }
        } else {
            sendError(TranslateManager.getTranslate("${this.javaClass.name}.error2")!!, obj = this)
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
