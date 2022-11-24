package com.github.q16695

import com.github.q16695.notifications.Notification

fun main(args: Array<String>) {
    Notification.create()
        .hideAfter(2000)
        .title("Small Tools")
        .text("NN")
        .showInformation()
}