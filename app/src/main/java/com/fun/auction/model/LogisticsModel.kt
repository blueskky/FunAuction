package com.`fun`.auction.model


data class LogisticsModel(
    val last_id: Int = 0, // 6
    val list: MutableList<LogisticsItem> = mutableListOf()
)

data class LogisticsItem(
    val box_id: Int = 0, // 1132
    val box_info: BoxInfo? = null,
    val create_time: Long = 0, // 1621152240
    val delivery_status: Int = 0, // 10
    val delivery_time: Long = 0, // 0
    val express_company: String = "", // yunda
    val express_no: String = "", // 4314495340502
    val express_price: String = "", // 0.00
    val order_id: String = "", // 14
    val order_no: String = "", // 2021051616035100110060874613
    val order_status: Int = 0, // 10
    val pay_price: String = "", // 0.00
    val pay_status: Int = 0, // 20
    val pay_time: Long = 0, // 0
    val receipt_status: Int = 0, // 10
    val receipt_time: Long = 0, // 0
    val total_price: String = "", // 0.00
    val transaction_id: String = "",
    val update_time: Long = 0, // 1621152406
    val user_id: Int = 0, // 10060
    val wxapp_id: Int = 0,// 10001

)

data class BoxInfo(
    val box_id: Int = 0, // 1132
    val box_no: String = "", // 2021051616035100110060874613
    val box_status: Int = 0, // 2
    val content: String = "",
    val create_time: Long = 0, // 1621152231
    val deduct_stock_type: Int = 0, // 20
    val delivery_status: Int = 0, // 10
    val delivery_time: Long = 0, // 0
    val express_company: String = "",
    val express_no: String = "",
    val express_price: String = "", // 0.00
    val goods_attr: String = "",
    val goods_id: Int = 0, // 285
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/2021051017124680c6b5685.jpg
    val goods_name: String = "", // 牛油果绿4层双耳带防护口罩20只
    val goods_no: String = "",
    val goods_price: String = "", // 12.80
    val goods_spec_id: Int = 0, // 364
    val goods_weight: Double = 0.0, // 0.20000000000000001
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
    val update_time: Long = 0, // 1621152240
    val user_id: Int = 0 // 10060
)
