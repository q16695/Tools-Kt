package com.github.q16695.utils

class Key {
    var string: String
    var clazz: Class<*>

    constructor(key: String, clazz: Any) {
        string = key
        this.clazz = clazz.javaClass
    }

    constructor(key: String, clazz: Class<*>) {
        string = key
        this.clazz = clazz
    }
}
