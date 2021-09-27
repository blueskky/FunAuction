package com.`fun`.auction.model

data class PayConfig(
    val appid: String,
    val noncestr: String,
    val `package`: String,
    val partnerid: String,
    val prepayid: String,
    val sign: String,
    val timestamp: String
)