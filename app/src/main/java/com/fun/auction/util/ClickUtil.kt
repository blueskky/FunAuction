package com.`fun`.auction.util

import android.os.Handler
import android.view.View

class ClickUtil {

    companion object{
        private val MIN_DELAY_TIME = 1000 // 两次点击间隔不能少于1000ms

        private var lastClickTime: Long = 0

        fun isFastClick(): Boolean {
            var flag = false
            val timeMillis = System.currentTimeMillis()
            if (timeMillis - lastClickTime < MIN_DELAY_TIME) {
                flag = true
            }
            lastClickTime = timeMillis
            return flag
        }


        fun blockClick(view: View?) {
            view?.isEnabled = false
            Handler().postDelayed({
                view?.isEnabled = true
            }, 1000)
        }

        fun blockClick(view: View?, time: Int) {
            view?.isEnabled = false
            Handler().postDelayed({
                view?.isEnabled = true
            }, time.toLong())
        }
    }


}