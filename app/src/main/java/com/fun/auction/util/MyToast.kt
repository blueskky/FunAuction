package com.`fun`.auction.util

import android.content.Context
import android.os.Handler

import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.`fun`.auction.R
import es.dmoral.toasty.Toasty


class MyToast {


    companion object {
        var context: Context? = null
        var lastToast: Toast? = null

        fun init(mContext: Context) {
            context = mContext
            Toasty.Config.getInstance().allowQueue(false).apply()
        }

        var mainHandler: Handler? = null
            get() {
                if (field == null) {
                    field = Handler(Looper.getMainLooper())
                }
                return field
            }


        fun isMainThread(): Boolean {
            val id = Thread.currentThread().id
            if (context == null) {
                return false
            }
            return context?.mainLooper?.thread?.id == id
        }

        fun runSafe(runable: Runnable) {
            if (isMainThread()) {
                runable.run()
            } else {
                mainHandler?.post(Runnable {
                    runable.run()
                })
            }
        }


        fun success(msg: String) {
            if (TextUtils.isEmpty(msg)) {
                return
            }
            context?.let {
                runSafe(Runnable { Toasty.success(it, msg, Toast.LENGTH_SHORT, true).show() })
            }
        }

        fun error(msg: String) {
            if (TextUtils.isEmpty(msg)) {
                return
            }
            context?.let {
                runSafe(Runnable { Toasty.error(it, msg, Toast.LENGTH_SHORT, true).show() })
            }
        }

        fun info(msg: String) {
            if (TextUtils.isEmpty(msg)) {
                return
            }
            context?.let {
                runSafe(Runnable { Toasty.info(it, msg, Toast.LENGTH_SHORT, true).show() })
            }
        }


        fun warn(msg: String) {
            if (TextUtils.isEmpty(msg)) {
                return
            }
            context?.let {
                runSafe(Runnable { Toasty.warning(it, msg, Toast.LENGTH_LONG, true).show() })
            }
        }

        fun show(msg: String) {
            if (TextUtils.isEmpty(msg)) {
                return
            }
            context?.let {
                runSafe(Runnable {
                    val toast = Toast.makeText(it, "", Toast.LENGTH_SHORT)
                    val view: View = LayoutInflater.from(context).inflate(R.layout.tip_layout, null)
                    val tvMessage = view.findViewById<TextView>(R.id.tv_message)
                    tvMessage.text = msg
                    toast?.view = view
                    toast?.setGravity(Gravity.CENTER, 0, 0)
                    lastToast?.cancel()
                    lastToast = toast
                    toast?.show()
                })
            }
        }

        fun showBottom(msg: String) {
            if (TextUtils.isEmpty(msg)) {
                return
            }
            context?.let {
                runSafe(Runnable {
                    val toast = Toast.makeText(it, "", Toast.LENGTH_SHORT)
                    val view: View = LayoutInflater.from(context).inflate(R.layout.tip_layout, null)
                    val tvMessage = view.findViewById<TextView>(R.id.tv_message)
                    tvMessage.text = msg
                    toast.view = view
                    toast.setGravity(Gravity.BOTTOM, 0, 100)
                    lastToast?.cancel()
                    lastToast = toast
                    toast.show()
                })
            }
        }


    }
}