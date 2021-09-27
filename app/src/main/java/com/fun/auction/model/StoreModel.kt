package com.`fun`.auction.model


data class StoreData(
    val list: MutableList<Product>,
    var page: Int,
    var peek: Int
)


data class Product(
    val auction_phone: String = "", // 132****6297
    val auction_price: String = "", // 4295.00
    var auction_time: Long = 0, // 1621962311
    val auction_user_id: Int = 0, // 9
    val deduct_stock_type: Int = 0, // 20
    val delivery_id: Int = 0, // 1
    val flag: Int = 0, // 0
    val flag_tag: Int = 0, // 0
    val goods_attr: String = "",
    val goods_id: Int = 0, // 93
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/202105051424423ac820664.jpg
    val goods_name: String = "", // 科沃斯EcovacsN9+智能拖洗一体扫地机器人
    val goods_no: String = "",
    val goods_price: String = "", // 4299.00
    val goods_spec_id: Int = 0, // 231
    val goods_weight: Int = 0, // 2
    val images: List<String> = listOf(),
    val is_hook: Int = 0, // 0
    val is_hot: Int = 0, // 10
    val line_price: String = "", // 0.00
    val match_id: Int = 0, // 0
    val peek_price: String = "", // 1.00
    val product_id: Int = 0, // 423
    val product_type: Int = 0, // 20
    val push_status: Int = 0, // 20
    val spec_sku_id: String = "",
    val spec_type: Int = 0, // 10
    val sub_name: String = "",
    var update_time: Long = 0, // 1621996430
    val store_status: Int = 0,
    var current_price: String? = null

)



