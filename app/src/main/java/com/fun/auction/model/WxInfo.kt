package com.`fun`.auction.model

data class WxInfo(
    val city: String = "", // 武汉
    val country: String = "", // 中国
    val headimgurl: String = "", // https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLXNW7QEehZW05SjLdlQFnhU2akmQB38RlzAsqcicwdJBUb15YBdlQOjb5Kt7LhWuv8YKNLSlX8jBg/132
    val language: String = "", // zh_CN
    val nickname: String = "", // LL
    val openid: String = "", // o8x9t6e2aJ_ipfYVRtj0693zaoIE
    val privilege: List<Any> = listOf(),
    val province: String = "", // 湖北
    val sex: String = "", // 1
    val unionid: String = "" // ofv3Y1Skr6rI94-Sf6gMOPSiZUz4
)