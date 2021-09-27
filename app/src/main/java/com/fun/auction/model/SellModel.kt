package com.`fun`.auction.model

class SellModel(
    val list: MutableList<SellItem>,
    val last_id: Int
)

data class SellItem(
    val box_id: Int = 0, // 564
    val box_no: String = "", // 202105111247300011386270
    val box_status: Int = 0, // 1
    val content: String = "",
    val create_time: Long = 0, // 1620708450
    val deduct_stock_type: Int = 0, // 20
    val delivery_status: Int = 0, // 10
    val delivery_time: Long = 0, // 0
    val express_company: String = "",
    val express_no: String = "",
    val express_price: String = "", // 0.00
    val goods_attr: String = "",
    val goods_id: Int = 0, // 102
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/20210427182103ec4507379.jpg
    val goods_name: String = "", // 招财猫情侣碗筷套装
    val goods_no: String = "",
    val goods_price: String = "", // 6.70
    val goods_spec_id: Int = 0, // 300
    val goods_weight: Double = 0.0, // 0.080000000000000002
    val is_hot: Int = 0, // 10
    val line_price: String = "", // 0.00
    val match_id: Int = 0, // 1
    val phone: String = "", // 188****6608
    val receipt_status: Int = 0, // 10
    val receipt_time: Long = 0, // 0
    val sell_price: String = "", // 0.00
    val sell_status: Int = 0, // 1
    val sell_time: Long = 0, // 0
    val spec_sku_id: String = "",
    val spec_type: Int = 0, // 10
    val total_num: Int = 0, // 1
    val total_price: String = "", // 0.00
    val type_status: Int = 0, // 10
    val update_time: Long = 0, // 1620749888
    val user_id: Int = 0 // 1
)