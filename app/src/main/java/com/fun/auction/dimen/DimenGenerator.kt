package com.`fun`.auction.dimen

import java.io.File


val DESIGN_WIDTH = 1080

fun main(args: Array<String>) {
    for (value in 300..600 step 30) {
        val file = File("")
        MakeUtils.makeAll(DESIGN_WIDTH, value, file.absolutePath)
    }

    val file = File("")
    MakeUtils.makeAll(DESIGN_WIDTH, 530, file.absolutePath)
}