package com.`fun`.auction.model

data class PartnerModel(
    val hhr_url: String = "", // https://hhr.qupaipai.com/partner/home/index
    val invite_num: Int = 0, // 0
    val invite_url: String = "", // https://www.qupaipaia.com?invite_id=10060
    val is_hhr: Int = 0, // -1
    val list: MutableList<Long> = mutableListOf(),
    var listleng: Int = 0 // 10
)

class Partner {

}