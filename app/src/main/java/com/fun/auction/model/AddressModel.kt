package com.`fun`.auction.model

import java.io.Serializable

data class AddressModel(
    val default_id: Int? = null, // 7
    val list: MutableList<Address> = mutableListOf()

)

data class Address(
    val address_id: Int = 0, // 7
    val city_id: Int = 0, // 1710
    val create_time: Long = 0, // 0
    val detail: String = "", // 三栋一单元
    val name: String = "", // 露露
    val phone: String = "", // 13212726297
    val province_id: Int = 0, // 1709
    val region: String = "", // 湖北省 武汉市 武昌区
    val region_id: Int = 0, // 1715
    val update_time: Long = 0, // 0
    val user_id: Int = 0,// 10060
    var default: Boolean = false
) : Serializable