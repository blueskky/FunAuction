package com.`fun`.auction.net

import android.text.TextUtils
import com.`fun`.auction.BuildConfig
import com.`fun`.auction.Constants
import com.`fun`.auction.PrefercencesManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.ScreenUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class AuthInterceptor : Interceptor {


    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val newBuilder = chain.request().newBuilder()
        addHeaders(newBuilder)
        val newRequest = newBuilder.build()
        val proceed = chain.proceed(newRequest)
        val responseBodyCopy = proceed.peekBody(Long.MAX_VALUE)
        val responseStr = responseBodyCopy.string()

        val expired = isExpired(responseStr)
        if (expired) {
            val newAuth = getNewAuth()
            if (!newAuth.isNullOrEmpty()) {
                proceed.close()
                val build = chain.request().newBuilder()
                addHeaders(build)
                val request = build.build()
                return chain.proceed(request)
            }
        }
        return proceed
    }

    private fun addHeaders(builder: Request.Builder) {
        builder.header("token", HttpManager.token)
        var auth = PrefercencesManager.getInstance()?.getAuthorization()
        if (!TextUtils.isEmpty(auth)) {
            builder.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MjU1MDAwOTcsInN1YiI6ImFjY2Vzcy10b2tlbiIsIm5iZiI6MTYyNTQ5Mjg5NywiYXVkIjoidXNlciIsImlhdCI6MTYyNTQ5Mjg5NywianRpIjoiMTBjZmMwNjBiZDI5NDEwNmYwNjIxMTY4NWMwNWNhNTgiLCJpc3MiOiJhdXRoIiwic3RhdHVzIjoxLCJkYXRhIjp7InVzZXJfaWQiOjEwMDU4LCJsb2dpbiI6dHJ1ZSwibGFzdF90aW1lIjoxNjI1NDkyODk3fX0.tex6dDTInlEvatYUrB712jm9d_mFBXAeT1sQlLdZsYI")
        }

        builder.header("device-name", DeviceUtils.getModel())
        builder.header("width", ScreenUtils.getScreenWidth().toString())
        builder.header("height", ScreenUtils.getScreenHeight().toString())
        builder.header("os", "Android")
        builder.header("os-version", DeviceUtils.getSDKVersionName())
        builder.header("ver", AppUtils.getAppVersionCode().toString())
        builder.header("ver-sp", BuildConfig.VERSION_SP)
    }

    private fun getNewAuth(): String? {
        val map = mutableMapOf<String, String>()
        map["account"] = PrefercencesManager.getInstance()?.getAccount() ?: ""
        map["password"] = PrefercencesManager.getInstance()?.getPwd() ?: ""
        val call = HttpManager.api?.refresh(map)
        val response = call?.execute()
        if (response?.body() != null) {
            val code = response.body()?.code
            if (code == 0) {
                val accessToken = response.body()?.result?.access_token
                PrefercencesManager.getInstance()?.setAuthorization(accessToken)
                return accessToken
            }
        }
        return null
    }

    private fun isExpired(responseStr: String): Boolean {
        try {
            val jsonObject = JSONObject(responseStr)
            val code = jsonObject.optInt("code")
            if (9999 == code) {
                return true
            }

        } catch (e: Exception) {
        }
        return false
    }


}