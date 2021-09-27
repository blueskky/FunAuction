package com.`fun`.auction.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log


class MyService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return MyBind()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("lhq", "Service  onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("lhq", "Service  onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }


    class MyBind : Binder() {
        fun openSocket(websocketUrl: String) {
            WebSocketManagerKt.init(websocketUrl)
        }

        fun stopSocket() {
            WebSocketManagerKt.close()
        }
    }
}