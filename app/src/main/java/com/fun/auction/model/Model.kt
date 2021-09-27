package com.`fun`.auction.model

open class Model<T>(
    var msg: String = "",
    var code: Int = 0,
    var result: T? = null
) {
    val success: Boolean
        get() = code == 0
}