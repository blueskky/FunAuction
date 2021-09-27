package com.`fun`.auction.model

import java.text.SimpleDateFormat

data class LogisticeModel(
    val com: String = "", // yunda
    val info: MutableList<Info> = mutableListOf(),
    val nu: String = "", // 4314495340502
    val state: Int = 0, // 1
    val no_info: NoInfo? = null
)

data class Info(
    var content: String = "", // [广东深圳公司中心分拨分部]【深圳市】已离开 广东深圳公司中心分拨分部；发往 武昌地区包
    val time: String = "" // 2021-05-15 23:02
) : Comparable<Info> {

    override fun compareTo(other: Info): Int {
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val time = simpleDateFormat.parse(time).time
            val time2 = simpleDateFormat.parse(other.time).time
            return (time2 - time).toInt()
        } catch (e: Exception) {
        }
        return 0
    }
}


data class NoInfo(
    val express_company: String? = null, // 其他快递公司
    val express_no: String? = null // YT544623987295
)