package com.`fun`.auction.model



data class ChargeModel(
    val list: MutableList<ChargeItem>,
    val pay_way: List<PayWay>
)

data class ChargeItem(
    val amount: Double,
    val experience: Int,  //赠送经验
    val give_qty: Int,    //赠送数量
    val item_name: String,
    val qty: Int,
    val unlock_id: Int
)

data class PayWay(
    val name: String,
    val status: Int,
    val type: String
)