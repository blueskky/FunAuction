package com.`fun`.auction.model

data class PeekModel(
    val list: Peek
)

data class Peek(
    val current_price: String = "", // 358.5
    val dec_price: String = "", // 0.5
    val game_over: Int = 0, // 0
    val peek_price: String = "" // 1.00
)