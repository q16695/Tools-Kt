package com.github.q16695

import com.github.q16695.commands.*
import com.github.q16695.events.*
import com.github.q16695.managers.ConfigManager
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.LogManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.*
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import java.net.URL
import java.sql.Date
import java.time.Instant
import java.util.*
import javax.swing.*
import kotlin.collections.ArrayList

class Main {
    var The_Core_Values_Of_Chinese_Socialism = ArrayList<String>()
    //var url = URL("http://img.xjh.me/random_img.php")
    var titleFont: Font
    var tip = JLabel("Tip: ")
    var eventManager: EventManager = EventManager()
    var background: JLabel? = null
    var frame = JFrame("Small Tools")
    var icon: ImageIcon
    var textFont: Font
    var configmanager = ConfigManager()
    var color = Color.ORANGE
    var selectJLabel: JButton? = null
    var enteredString: String? = null
    var command = JTextField(20)
    var TCVOCS = 0
    var antiblur = AntiBlur
    var clickedList = ArrayList<JLabel>()
    var extra = ArrayList<JLabel>()
    var time = JLabel("Time" + Date.from(Instant.now()))
    var basicCommands: ArrayList<BasicCommand> = ArrayList()
    var lastCommandText: String? = null
    var currentCommandText: String? = null
    var translateFile = "en_US"
    var image: Image? = null
    var helpList = ArrayList<JButton>()
    var translate: MutableMap<String, String> = HashMap()

    companion object {
        var config: Config = Config(File(" ").canonicalPath + "config")
        var Instance: Main? = null
        var notification = false
        var mainName = "Small Tools"
        var ready = false
    }

    var mouseInFrame = false

    private fun setInstance() {
        Instance = this
    }

    fun getDownButton(buttons: ArrayList<JButton>, button: JButton): JButton? {
        if (buttons.isEmpty()) return null
        for (c in buttons) {
            if (c.y == button.y + 32) {
                return c
            }
        }
        return null
    }

    fun getUpButton(buttons: ArrayList<JButton>, button: JButton): JButton? {
        if (buttons.isEmpty()) return null
        for (c in buttons) {
            if (c.y == button.y - 32) {
                return c
            }
        }
        return null
    }

    fun getTheBasicCommand(command: String): BasicCommand? {
        for (c in basicCommands) {
            if (command.contains(" ")) {
                if (command.lowercase().substring(0, command.lowercase().indexOf(" ")) == c.name.toLowerCase(Locale.ROOT)) {
                    return c
                }
            } else {
                if (command.lowercase() == c.name.toLowerCase(Locale.ROOT)) {
                    return c
                }
            }
        }
        return null
    }

    fun isCommand(command: String): Boolean {
        for (c in basicCommands) {
            if (command.contains(" ")) {
                if (command.lowercase().substring(0, command.lowercase().indexOf(" ")) == c.name.toLowerCase(Locale.ROOT)) {
                    return true
                }
            } else {
                if (command.lowercase() == c.name.toLowerCase(Locale.ROOT)) {
                    return true
                }
            }
        }
        return false
    }

    private fun init() {
        basicCommands.add(AntiBlurCommand())
        basicCommands.add(HelpCommand())
        basicCommands.add(MD5Command())
        basicCommands.add(ChangeNotificationCommand())
        basicCommands.add(ConfigCommand())
        basicCommands.add(ImageCommand())
        basicCommands.add(KuGouCommand())
        The_Core_Values_Of_Chinese_Socialism.add("富强")
        The_Core_Values_Of_Chinese_Socialism.add("民主")
        The_Core_Values_Of_Chinese_Socialism.add("文明")
        The_Core_Values_Of_Chinese_Socialism.add("和谐")
        The_Core_Values_Of_Chinese_Socialism.add("自由")
        The_Core_Values_Of_Chinese_Socialism.add("平等")
        The_Core_Values_Of_Chinese_Socialism.add("公正")
        The_Core_Values_Of_Chinese_Socialism.add("法治")
        The_Core_Values_Of_Chinese_Socialism.add("爱国")
        The_Core_Values_Of_Chinese_Socialism.add("敬业")
        The_Core_Values_Of_Chinese_Socialism.add("诚信")
        The_Core_Values_Of_Chinese_Socialism.add("友善")
    }

    init {
        init()
        configmanager.add(Setting("Translate",translateFile))
        for (s in FileUtils.getFileAllContent1(this.javaClass.getResourceAsStream("/translate/$translateFile"))) {
            translate[s.substring(0, s.indexOf(" "))] = s.substring(s.indexOf("=") + 2)
        }
        configmanager.add("MainName", "Small Tools")
        titleFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(this.javaClass.getResourceAsStream("/assets/barkeliy-nrgog.ttf")))
        //titleFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/Orbitron-Regular.ttf")));
        titleFont = titleFont.deriveFont(Font.ITALIC, 64f)
        //textFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/LexendDeca-Regular.ttf")));
        textFont = Font.createFont(Font.TRUETYPE_FONT, this.javaClass.getResourceAsStream("/assets/ruizitaikongzhumengheichaohei.ttf"))
        textFont = textFont.deriveFont(Font.TRUETYPE_FONT, 16f)

        icon = ImageIcon(Objects.requireNonNull(this.javaClass.getResource("/assets/icon.png")))

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack()
        frame.isVisible = true
        frame.setLocation(Toolkit.getDefaultToolkit().screenSize.width / 4, Toolkit.getDefaultToolkit().screenSize.height / 4)
        frame.setSize(800, 400)
        frame.layout = null

        val json = "https://api.ghser.com/random/api.php"
        val bg = ImageIcon(URL(json))
        background = JLabel(bg)
        background!!.setSize(bg.iconWidth, bg.iconHeight)

        frame.layeredPane.add(background!!, Integer.MIN_VALUE)

        val pan = frame.contentPane as JPanel
        pan.isOpaque = false

        val textcommand = JLabel("Command: ")
        textcommand.setBounds(10, 310, 80, 25)
        frame.add(textcommand)
        textcommand.font = textFont
        command.setBounds(100, frame.height - 90, 310, 25)
        frame.add(command)
        time.setBounds(frame.height - 20, 20, 310, 25)
        frame.add(time)
        val name = JLabel("Small Tool")
        name.setBounds(10, frame.height - 300, 310, 25)
        frame.add(name)
        name.font = titleFont
        tip.setBounds(10, frame.height - 750, 400, 300)
        frame.add(tip)
        tip.font = textFont
        frame.iconImage = icon.image

        frame.addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent) {}
            override fun mousePressed(e: MouseEvent) {
                MouseEvent(e.button, true, Vec2d(MouseInfo.getPointerInfo().location.x,MouseInfo.getPointerInfo().location.y)).post()
            }

            override fun mouseReleased(e: MouseEvent) {
                MouseEvent(e.button, false, Vec2d(MouseInfo.getPointerInfo().location.x,MouseInfo.getPointerInfo().location.y)).post()
            }

            override fun mouseEntered(e: MouseEvent) {
                mouseInFrame = true
            }

            override fun mouseExited(e: MouseEvent) {
                mouseInFrame = false
            }
        })
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { ke: KeyEvent ->
            if (ke.id == KeyEvent.KEY_PRESSED) {
                KeyboardEvent(ke.keyCode, true).post()
            } else if (ke.id == KeyEvent.KEY_RELEASED) {
                KeyboardEvent(ke.keyCode, false).post()
            }
            false
        }
        TranslateManager.setTranslate(translate)

        MessageUtils.sendNotification(translate[this.javaClass.name + ".hello1"]!!,this)

        frame.extendedState = JFrame.MAXIMIZED_BOTH
        setInstance()
        Thread {
            while (frame.isVisible) {
                val threadEvent = ThreadEvent(5L)
                threadEvent.post()
                Thread.sleep(threadEvent.delay)
            }
        }.start()
        Thread {
            while (frame.isVisible) {
                TickEvent().post()
                TickEvent.Pre().post()
                Thread.sleep(50L)
                TickEvent.Post().post()
            }
        }.start()
        ready = true
        while (frame.isVisible) {
            antiblur = AntiBlur
            if(background != null && frame.layeredPane.getIndexOf(background!!) != -1) {
                frame.layeredPane.setLayer(background!!, Integer.MIN_VALUE)
            }
            command.font = textFont
            lastCommandText = currentCommandText
            currentCommandText = command.text
            command.isOpaque = true
            command.background = Color.gray
            image = bg.image

            val tick_color = floatArrayOf(System.currentTimeMillis() % 11520L / 11520.0f)
            val color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f)
            color = Color(color_rgb_o shr 16 and 0xFF, color_rgb_o shr 8 and 0xFF, color_rgb_o and 0xFF)

            if (frame.height > 750) {
                tip.setBounds(10, frame.height - 750, frame.width, 300)
            } else {
                tip.setBounds(10, 20, frame.width, 300)
            }
            textcommand.setBounds(10, frame.height - 90, frame.width - 490, 25)
            command.setBounds(120, frame.height - 90, frame.width - 490, 25)
            name.setBounds(15, 5, 800, 100)
            name.text = mainName
            frame.title = mainName
            time.setBounds(frame.width - 620, 20, 800, 64)
            time.text = TranslateManager.getTranslate(this.javaClass.name + ".timeText")!!.replace("%var1%", Date.from(Instant.now()).toString())
            time.foreground = color
            time.font = titleFont
            tip.foreground = color
            textcommand.foreground = color
            name.foreground = color

            //tip.setText(MainExecute.tip);
            mainName = configmanager.getSetting("MainName")!!.value
            setInstance()
            if(configmanager.getSetting("Notification") != null) {
                notification = configmanager.getSetting("Notification")!!.value.toBoolean()
            }
        }
        configmanager.save()
        ready = false
    }
}
