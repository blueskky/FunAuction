package com.`fun`.auction.model

data class PatModel(
    val last_id: Int,
    val list: MutableList<PatRecord>
)


data class PatRecord(
    val auction_price: String = "", // 4298.50
    val auction_time: Long = 0, // 1622084481
    val auction_user_id: Int = 0, // 62
    val coin_val: String = "", // 1.00
    val content: String = "",
    val cost_type: Int = 0, // 10
    val create_time: Long = 0, // 1622082290
    val current_price: String = "", // 4298.50
    val dec_price: String = "", // 0.50
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
    val id: Int = 0, // 467
    val images: List<String> = listOf(),
    val is_hook: Int = 0, // 0
    val is_hot: Int = 0, // 10
    val is_over: Int = 0, // 0
    val line_price: String = "", // 0.00
    val match_id: Int = 0, // 0
    val my_store_money: String = "", // 4298.50
    val original_price: String = "", // 4299.00
    val peek_num: Int = 0, // 2
    val peek_price: String = "", // 1.00
    val product_id: Int = 0, // 431
    val product_type: Int = 0, // 20
    val push_status: Int = 0, // 20
    val spec_sku_id: String = "",
    val spec_type: Int = 0, // 10
    val store_down_time: Long = 0, // 1622085422
    val store_price_time: Long = 0, // 1622084481
    val store_product_id: Int = 0, // 431
    val store_status: Int = 0, // 3
    val store_user: Int = 0, // 62
    val store_user_phone: String = "", // 134****0250
    val sub_name: String = "",
    val update_time: Long = 0, // 1622085422
    val user_id: Int = 0,// 62
    val auction_phone: String = ""
)


