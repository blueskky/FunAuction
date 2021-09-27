package com.`fun`.auction.model

data class PatEndModel(
    val last_id: Int = 0, // 1706
    val list: MutableList<PatEnd> = mutableListOf()
)


data class PatEnd(
    val box_id: Int = 0, // 1799
    val box_no: String = "", // 2021052710272800062735204
    val box_status: Int = 0, // 0
    val content: String = "",
    val create_time: Long = 0, // 1622082448
    val deduct_stock_type: Int = 0, // 20
    val delivery_status: Int = 0, // 10
    val delivery_time: Int = 0, // 0
    val express_company: String = "",
    val express_no: String = "",
    val express_price: String = "", // 0.00
    val goods_attr: String = "",
    val goods_id: Int = 0, // 430
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/202105051424423ac820664.jpg
    val goods_name: String = "", // 科沃斯EcovacsN9+智能拖洗一体扫地机器人
    val goods_no: String = "",
    val goods_price: String = "", // 4299.00
    val goods_spec_id: Int = 0, // 231
    val goods_weight: Int = 0, // 2
    val images: String = "",
    val is_hot: Int = 0, // 10
    val line_price: String = "", // 0.00
    val match_id: Int = 0, // 0
    val peek_num: Int = 0, // 3
    val phone: String = "", // 134****0250
    val receipt_status: Int = 0, // 10
    val receipt_time: Int = 0, // 0
    val sell_price: String = "", // 0.00
    val sell_status: Int = 0, // 0
    val sell_time: Int = 0, // 0
    val spec_sku_id: String = "",
    val spec_type: Int = 0, // 10
    val store_num: Int = 0, // 0
    val total_num: Int = 0, // 1
    val total_price: String = "", // 0.00
    val type_status: Int = 0, // 30
    val update_time: Long = 0, // 0
    val user_id: Int = 0 // 62
)