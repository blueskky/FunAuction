package com.`fun`.auction.model

data class TaskModel(
    var jd: Map<String, Task>,

    var prize: Goods? = null
)

data class Task(
    var award: Int = 0, // 0
    val limit: Int = 0, // 10
    val num: Int = 0 // 10
){
    fun activate(): Boolean {
        return num >= limit
    }

    fun received(): Boolean {
        if (num >= limit) {
            if (award == 0) {
                return true
            }
        }
        return false
    }
}






