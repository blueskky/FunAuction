package com.`fun`.auction.model


data class RecordDetail(
    val box_id: Int = 0, // 1780
    val box_no: String = "", // 202105260050220019286104
    val box_status: Int = 0, // 0
    val content: String = "",
    val create_time: Long = 0, // 1621961422
    val deduct_stock_type: Int = 0, // 20
    val delivery_status: Int = 0, // 10
    val delivery_time: Long = 0, // 0
    val express_company: String = "",
    val express_no: String = "",
    val express_price: String = "", // 0.00
    val goods_attr: String = "",
    val goods_id: Int = 0, // 298
    val goods_image: String = "", // https://pfile.qupaipaia.com/qupaipai/goods/20210429132244444943188.jpg
    val goods_name: String = "", // 桌面迷你电动吸尘器
    val goods_no: String = "",
    val goods_price: String = "", // 19.90
    val goods_spec_id: Int = 0, // 286
    val goods_weight: Double = 0.0, // 0.029999999999999999
    val images: ArrayList<String>? = arrayListOf(),
    val is_hot: Int = 0, // 30
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
    val user_id: Int = 0, // 9
    val order_address: OrderAddress? = null,
    val order_info: OrderInfo? = null,
)


data class OrderAddress(
    val city_id: Int = 0, // 1710
    val create_time: Long = 0, // 1621152240
    val detail: String = "", // 三栋一单元
    val name: String = "", // 露露
    val order_address_id: Int = 0, // 14
    val order_id: Int = 0, // 14
    val phone: String = "", // 13212726297
    val province_id: Int = 0, // 1709
    val region: String = "", // 湖北省 武汉市 武昌区
    val region_id: Int = 0, // 1715
    val user_id: Int = 0, // 10060
    val wxapp_id: Int = 0 // 10001
)

data class OrderInfo(
    val box_id: Int = 0, // 1132
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
    val wxapp_id: Int = 0 // 10001
)