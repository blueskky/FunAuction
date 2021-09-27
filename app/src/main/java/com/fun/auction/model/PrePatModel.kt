package com.`fun`.auction.model

data class PrePatModel(
    val list: PrePat? = PrePat()
)


data class PrePat(
    val auction_phone: String = "", // 132****6297
    val auction_price: String = "", // 4299
    val auction_time: Int = 0, // 1622350834
    val auction_user_id: Int = 0, // 9
    val current_price: String = "", // 4299.00
    val dec_price: String = "", // 0
    val game_over: Int = 0, // 9
    val peek_price: String = "", // 1.00
    val update_time: Any? = Any() // null
)