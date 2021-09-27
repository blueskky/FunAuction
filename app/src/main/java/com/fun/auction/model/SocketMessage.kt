package com.`fun`.auction.model

data class SocketMessage(
    val cbk: Int = 0, // 102
    val `data`: MessageContent = MessageContent()
)

data class MessageContent(
    val content: String = "", // 恭喜191****5542普通场拍中iphone 12mini 128G
    val fd: Int = 0 // 1
)