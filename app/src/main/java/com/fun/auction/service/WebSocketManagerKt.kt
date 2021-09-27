package com.`fun`.auction.service

import android.util.Log
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.model.BindMessage
import com.`fun`.auction.model.Data
import com.`fun`.auction.model.SocketMessage
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit


object WebSocketManagerKt {

    private var httpClient: OkHttpClient? = null
    private var newWebSocket: WebSocket? = null
    private var request: Request? = null
    private var activeClose: Boolean = false


    fun init(url: String) {
        httpClient = OkHttpClient.Builder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .pingInterval(10, TimeUnit.SECONDS)
            .build()
        request = Request.Builder().url(url).build()
        newWebSocket = httpClient?.newWebSocket(request, listener)
    }


    fun send(msg: String) {
        newWebSocket?.send(msg)
    }

    fun close() {
        newWebSocket?.cancel()
        activeClose = true
    }


    fun reConnect() {
        httpClient?.newWebSocket(request, listener)
    }


    private val listener by lazy {
        object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("lhq", "onOpen${response.body().toString()}")

                val authorization = PrefercencesManager.getInstance()?.getAuthorization()
                val message = BindMessage(data = Data(access_token = authorization))
                val toJson = Gson().toJson(message)
                send(toJson)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                try {
                    val message = Gson().fromJson<SocketMessage>(text, SocketMessage::class.java)
                    EventBus.getDefault().post(message)
                    Log.d("lhq", "onMessage${text}")
                } catch (e: Exception) {

                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("onClosing", "reason=${reason}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("onClosed", "reason=${reason}")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("onFailure", "=${t.message}")

                if (t.message != null && t.message == "Socket closed") {
                    return
                }
                if (activeClose) {
                    return
                }
                reConnect()
            }
        }
    }

}