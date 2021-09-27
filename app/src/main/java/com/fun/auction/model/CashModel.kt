package com.`fun`.auction.model

data class CashModel(
    val list: MutableList<CashLog>,
    val last_id: Int? = null
)

data class CashLog(
    val cash_type: Int = 0, // 3
    val cash_user: String = "", // o8x9t6e2aJ_ipfYVRtj0693zaoIE
    val create_time: Long = 0, // 1622359697
    val id: Int = 0, // 2
    val id_card: String = "",
    val money: Int = 0, // 10000
    val msg: String = "",
    val oper: Int = 0, // 0
    val order_m: String = "",
    val order_o: String = "",
    val status: Int = 0, // 0
    val true_name: String = "", // 露露
    val update_time: Int = 0, // 0
    val user_id: Int = 0 // 9

) {


    fun getType(): String {
        return when (cash_type) {
            1 -> "支付宝提现"
            2, 3 -> "微信提现"
            else -> ""
        }
    }
}