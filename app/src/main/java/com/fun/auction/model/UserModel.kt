package com.`fun`.auction.model

data class UserModel(
    val user: User
)

data class User(
    val account: String = "", // 3aef7161f326c4d9821c386dc38ad2b4
    val avatar_url: String = "",
    val bind_code: Int = 0, // 0
    val create_time: Long = 0, // 1618820390
    val experience: Int = 0, // 300
    val gender: Int = 0, // 0
    val id: Int = 0, // 5
    val is_status: Int = 0, // 1
    val nice_name: String = "",
    val phone: String = "", // 18871486608
    val score: Int = 0, // 6081
    val update_time: Long = 0, // 0
    val user_id: Int = 0, // 1
    val version: String = "" // 1.0.0
)