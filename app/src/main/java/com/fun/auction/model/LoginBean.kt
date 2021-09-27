package com.`fun`.auction.model


data class LoginAuth(
    val access_token: String = "", // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjIxNzUyNjAsInN1YiI6ImFjY2Vzcy10b2tlbiIsIm5iZiI6MTYyMjE2ODA2MCwiYXVkIjoidXNlciIsImlhdCI6MTYyMjE2ODA2MCwianRpIjoiMTBjZmMwNjBiZDI5NDEwNmYwNjIxMTY4NWMwNWNhNTgiLCJpc3MiOiJhdXRoIiwic3RhdHVzIjoxLCJkYXRhIjp7InVzZXJfaWQiOjksImxvZ2luIjp0cnVlLCJsYXN0X3RpbWUiOjE2MjIxNjgwNjB9fQ.fBSORZKkVmFHpG82M64z3_nWrVHklSzjn74nHtJ4eSs
    val account: String = "", // 13212726297
    val expire_ts: Int = 0, // 1622175260
    val password: String = "", // 8bb41ef9161ae25d93c55aec813f6231
    val user_id: Int = 0 // 9
)