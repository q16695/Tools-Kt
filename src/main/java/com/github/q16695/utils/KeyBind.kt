package com.github.q16695.utils

class KeyBind(var keyCode: Int, var isDown: Boolean) {

    fun isDownKey(keyCode: Int): Boolean {
        return this.keyCode == keyCode && isDown
    }

    fun isKey(keyCode: Int): Boolean {
        return this.keyCode == keyCode
    }

    val isUp: Boolean
        get() = !isDown

    override fun toString(): String {
        return "KeyBind{" +
                "keyCode=" + keyCode +
                ", pressed=" + isDown +
                '}'
    }

    companion object {
        const val ENTER = '\n'.code
        const val BACK_SPACE = '\b'.code
        const val TAB = '\t'.code
        const val CANCEL = 0x03
        const val CLEAR = 0x0C
        const val SHIFT = 0x10
        const val CONTROL = 0x11
        const val ALT = 0x12
        const val PAUSE = 0x13
        const val CAPS_LOCK = 0x14
        const val ESCAPE = 0x1B
        const val SPACE = 0x20
        const val PAGE_UP = 0x21
        const val PAGE_DOWN = 0x22
        const val END = 0x23
        const val HOME = 0x24
        const val LEFT = 0x25
        const val UP = 0x26
        const val RIGHT = 0x27
        const val DOWN = 0x28
        const val COMMA = 0x2C
        const val MINUS = 0x2D
        const val PERIOD = 0x2E
        const val SLASH = 0x2F
        const val NUM0 = 0x30
        const val NUM1 = 0x31
        const val NUM2 = 0x32
        const val NUM3 = 0x33
        const val NUM4 = 0x34
        const val NUM5 = 0x35
        const val NUM6 = 0x36
        const val NUM7 = 0x37
        const val NUM8 = 0x38
        const val NUM9 = 0x39
        const val SEMICOLON = 0x3B
        const val EQUALS = 0x3D
        const val A = 0x41
        const val B = 0x42
        const val C = 0x43
        const val D = 0x44
        const val E = 0x45
        const val F = 0x46
        const val G = 0x47
        const val H = 0x48
        const val I = 0x49
        const val J = 0x4A
        const val K = 0x4B
        const val L = 0x4C
        const val M = 0x4D
        const val N = 0x4E
        const val O = 0x4F
        const val P = 0x50
        const val Q = 0x51
        const val R = 0x52
        const val S = 0x53
        const val T = 0x54
        const val U = 0x55
        const val V = 0x56
        const val W = 0x57
        const val X = 0x58
        const val Y = 0x59
        const val Z = 0x5A
        const val NUMPAD0 = 0x60
        const val NUMPAD1 = 0x61
        const val NUMPAD2 = 0x62
        const val NUMPAD3 = 0x63
        const val NUMPAD4 = 0x64
        const val NUMPAD5 = 0x65
        const val NUMPAD6 = 0x66
        const val NUMPAD7 = 0x67
        const val NUMPAD8 = 0x68
        const val NUMPAD9 = 0x69
        const val MULTIPLY = 0x6A
        const val ADD = 0x6B
        const val SEPARATER = 0x6C
        const val SUBTRACT = 0x6D
        const val DECIMAL = 0x6E
        const val DIVIDE = 0x6F
        const val DELETE = 0x7F
        const val NUM_LOCK = 0x90
        const val SCROLL_LOCK = 0x91
        const val F1 = 0x70
        const val F2 = 0x71
        const val F3 = 0x72
        const val F4 = 0x73
        const val F5 = 0x74
        const val F6 = 0x75
        const val F7 = 0x76
        const val F8 = 0x77
        const val F9 = 0x78
        const val F10 = 0x79
        const val F11 = 0x7A
        const val F12 = 0x7B
        const val F13 = 0xF000
        const val F14 = 0xF001
        const val F15 = 0xF002
        const val F16 = 0xF003
        const val F17 = 0xF004
        const val F18 = 0xF005
        const val F19 = 0xF006
        const val F20 = 0xF007
        const val F21 = 0xF008
        const val F22 = 0xF009
        const val F23 = 0xF00A
        const val F24 = 0xF00B
        const val PRINTSCREEN = 0x9A
        const val INSERT = 0x9B
        const val HELP = 0x9C
        const val META = 0x9D
        const val BACK_QUOTE = 0xC0
        const val QUOTE = 0xDE
        const val KP_UP = 0xE0
        const val KP_DOWN = 0xE1
        const val KP_LEFT = 0xE2
        const val KP_RIGHT = 0xE3
        const val DEAD_GRAVE = 0x80
        const val DEAD_ACUTE = 0x81
        const val DEAD_CIRCUMFLEX = 0x82
        const val DEAD_TILDE = 0x83
        const val DEAD_MACRON = 0x84
        const val DEAD_BREVE = 0x85
        const val DEAD_ABOVEDOT = 0x86
        const val DEAD_DIAERESIS = 0x87
        const val DEAD_ABOVERING = 0x88
        const val DEAD_DOUBLEACUTE = 0x89
        const val DEAD_CARON = 0x8a
        const val DEAD_CEDILLA = 0x8b
        const val DEAD_OGONEK = 0x8c
        const val DEAD_IOTA = 0x8d
        const val DEAD_VOICED_SOUND = 0x8e
        const val DEAD_SEMIVOICED_SOUND = 0x8f
        const val AMPERSAND = 0x96
        const val ASTERISK = 0x97
        const val QUOTEDBL = 0x98
        const val LESS = 0x99
        const val GREATER = 0xa0
        const val BRACELEFT = 0xa1
        const val BRACERIGHT = 0xa2
        const val AT = 0x0200
        const val COLON = 0x0201
        const val CIRCUMFLEX = 0x0202
        const val DOLLAR = 0x0203
        const val EURO_SIGN = 0x0204
        const val EXCLAMATION_MARK = 0x0205
        const val INVERTED_EXCLAMATION_MARK = 0x0206
        const val LEFT_PARENTHESIS = 0x0207
        const val NUMBER_SIGN = 0x0208
        const val PLUS = 0x0209
        const val RIGHT_PARENTHESIS = 0x020A
        const val UNDERSCORE = 0x020B
        const val WINDOWS = 0x020C
        const val CONTEXT_MENU = 0x020D
        const val FINAL = 0x0018
        const val CONVERT = 0x001C
        const val NONCONVERT = 0x001D
        const val ACCEPT = 0x001E
        const val MODECHANGE = 0x001F
        const val KANA = 0x0015
        const val KANJI = 0x0019
        const val ALPHANUMERIC = 0x00F0
        const val KATAKANA = 0x00F1
        const val HIRAGANA = 0x00F2
        const val FULL_WIDTH = 0x00F3
        const val HALF_WIDTH = 0x00F4
        const val ROMAN_CHARACTERS = 0x00F5
        const val ALL_CANDIDATES = 0x0100
        const val PREVIOUS_CANDIDATE = 0x0101
        const val CODE_INPUT = 0x0102
        const val JAPANESE_KATAKANA = 0x0103
        const val JAPANESE_HIRAGANA = 0x0104
        const val JAPANESE_ROMAN = 0x0105
        const val KANA_LOCK = 0x0106
        const val INPUT_METHOD_ON_OFF = 0x0107
        const val CUT = 0xFFD1
        const val COPY = 0xFFCD
        const val PASTE = 0xFFCF
        const val UNDO = 0xFFCB
        const val AGAIN = 0xFFC9
        const val FIND = 0xFFD0
        const val PROPS = 0xFFCA
        const val STOP = 0xFFC8
        const val COMPOSE = 0xFF20
        const val ALT_GRAPH = 0xFF7E
        const val BEGIN = 0xFF58
        const val UNDEFINED = 0x0
        const val CHAR_UNDEFINED = 0xFFFF.toChar()
        const val KEY_LOCATION_UNKNOWN = 0
        const val KEY_LOCATION_STANDARD = 1
        const val KEY_LOCATION_LEFT = 2
        const val KEY_LOCATION_RIGHT = 3
        const val KEY_LOCATION_NUMPAD = 4
    }
}
