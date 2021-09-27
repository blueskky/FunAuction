package com.`fun`.auction.model

data class MessageModel(
    val last_id: Int,
    val list: MutableList<SystemMsg>
)


data class SystemMsg(
    val create_time: Long = 0, // 1620622727
    val id: Int = 0, // 15
    val msg: String = "", // 测试：恭喜您！您的合伙人申请已通过，您的账号：xxxxxxx，初始密码：xxxxxxx；请前往微信公众号“趣拍拍Family”登录合伙人后台。
    val mtype: Int = 0, // 2
    var status: Int = 0, // 1
    val system_id: Int = 0, // 0
    val user_id: Int = 0 // 10060
)