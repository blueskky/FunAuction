package com.`fun`.auction

fun main(args: Array<String>) {

    val str = "abcdefg"

    str.let {
        it.substring(0, 1)
        return@let
    }

    println("结束")

}


fun twoSum(nums: IntArray, target: Int): IntArray {
    for (i in 0 until nums.size - 1) {
        val first = nums[i]
        val second = target - first
        for (j in (i + 1) until nums.size) {
            if (nums[j] == second) {
                return intArrayOf(i, j)
            }
        }
    }
    return intArrayOf()
}


fun reverse(x: Int): Int {
    var temp = x
    var answer: Long = 0
    while (temp != 0) {
        val pop = temp % 10
        answer = answer * 10 + pop
        temp /= 10
    }

    if (answer > Int.MAX_VALUE) {
        answer = 0
    }
    if (answer < Int.MIN_VALUE) {
        answer = 0
    }
    return answer.toInt()
}


fun isPalindrome(x: Int): Boolean {
    if (x < 0) {
        return false
    }

    var temp = x
    var answer = 0
    while (temp != 0) {
        var pop = temp % 10
        answer = answer * 10 + pop
        temp /= 10
    }

    if (answer == x) {
        return true
    }
    return false
}