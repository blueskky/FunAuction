package com.`fun`.auction.model

import java.io.Serializable

data class CashConfig(
    val bingo_type: Int = 0, // 0
    val surplus: Int = 0, // 100000
    val wx_appid: String = "", // wxa1e1df2750f810e7
    val wxapp_id: String = "", // o8x9t6e2aJ_ipfYVRtj0693zaoIE
    val wxapp_nick: String = "", // LL
    val wxapp_true_name: String = "", // 明吼吼
    val wxapp_user: String = "", // 13212222
    val zfb: String = "", // 123liubei
    val zfb_id: String = "",
    val zfb_true_name: String = "" // 刘备
) : Serializable