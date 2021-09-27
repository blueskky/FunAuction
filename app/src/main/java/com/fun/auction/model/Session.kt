package com.`fun`.auction.model

import java.io.Serializable


data class Session(
    val goods: MutableList<Goods> = mutableListOf(),
    val match_id: Int = 0, // 1
    val match_name: String = "", // 普通场
    val more_price: Int = 0, // 190
    val single_price: Int = 0 // 19
)

data class Goods(
    val box_id: Int = 0, // 1109
    val goods_id: Int = 0, // 273
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/20210506145635ec2dd7066.jpg
    val goods_name: String = "", // 小米巨能写中性笔十支装黑色
    val goods_price: Double = 0.0, // 9.9900000000000002
    val is_hot: Int = 0, // 20
    val total_num: Int = 0,// 0

) : Serializable


