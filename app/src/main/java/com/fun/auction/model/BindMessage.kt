package com.`fun`.auction.model

data class BindMessage(
    var controller: String = "Index/bind", // Index/bind
    var `data`: Data = Data()
)


data class Data(
    var client_id: String = "0",
    var access_token: String? = ""
)