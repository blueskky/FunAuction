package com.`fun`.auction.model

data class ChargeLogModel(
    val last_id: Int = 0, // 4
    val list: MutableList<ChargeLog> = mutableListOf()
)

data class ChargeLog(
    val amount: Int = 0, // 2000
    val give_qty: Int = 0, // 0
    val id: Int = 0, // 9
    val order_no: String = "", // 20210530154337000000093621
    val order_status: Int = 0, // 20
    val order_time: Long = 0, // 1622360617
    val payment: String = "", // balance
    val qty: Int = 0, // 20
    val unlock_id: Int = 0 // 800
) {
    fun getPayWay(): String {
        return when (payment) {
            "balance" -> "余额充值"
            "WeChat" -> "微信充值"
            "AliPay" -> "支付宝充值"
            else -> ""
        }
    }
}