package com.`fun`.auction.model

import android.provider.MediaStore


data class RecordModel(
    val list: MutableList<BoxRecord>,
    val last_id: Int,
    val is_sell: Int
)


data class BoxRecord(
    val box_id: Int = 0, // 1109
    val box_no: String = "", // 2021051515592700110060518706
    val box_status: Int = 0, // 0
    val content: String = "",
    val create_time: Long = 0, // 1621065567
    val deduct_stock_type: Int = 0, // 20
    val delivery_status: Int = 0, // 10
    val delivery_time: Long = 0, // 0
    val express_company: String = "",
    val express_no: String = "",
    val express_price: String = "", // 0.00
    val goods_attr: String = "",
    val goods_id: Int = 0, // 273
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/20210506145635ec2dd7066.jpg
    val goods_name: String = "", // 小米巨能写中性笔十支装黑色
    val goods_no: String = "",
    val goods_price: String = "", // 9.99
    val goods_spec_id: Int = 0, // 382
    val goods_weight: Double = 0.0, // 0.5
    val images: Any? = null,
    val is_hot: Int = 0, // 20
    val line_price: String = "", // 0.00
    val match_id: Int = 0, // 1
    val phone: String = "", // 132****6297
    val receipt_status: Int = 0, // 10
    val receipt_time: Long = 0, // 0
    val sell_price: String = "", // 0.00
    val sell_status: Int = 0, // 0
    val sell_time: Long = 0, // 0
    val spec_sku_id: String = "",
    val spec_type: Int = 0, // 10
    val total_num: Int = 0, // 1
    val total_price: String = "", // 0.00
    val type_status: Int = 0, // 10
    val update_time: Long = 0, // 0
    val user_id: Int = 0 // 10060
)

