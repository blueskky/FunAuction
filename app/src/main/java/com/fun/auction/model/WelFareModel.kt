package com.`fun`.auction.model

data class WelFareModel(
    val list: MutableList<WelFare> = mutableListOf(),
    val page: Int = 0 // 1
)

data class WelFare(
    val get_prize: String = "",
    val goods_image: String = "",
    val goods_name: String = "",
    val goods_prize: String = "",
    val images: List<String> = listOf(),
    val prize_id: Int = 0, // 1
    val user_prize: String = ""
)