package com.`fun`.auction.net

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory
import com.`fun`.auction.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class HttpManager {

    companion object {

        val token = "ea4e1efefa4b6b4f6be326930d0fc333"


        var api: Api? = null
            get() {
                if (field == null) {
                    create()
                }
                return field
            }

        private fun create() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .client(getOkHttpClient())
                .addConverterFactory(Retrofit2ConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
            api = retrofit.create(Api::class.java)
        }

        private fun getOkHttpClient(): OkHttpClient {
            val build = OkHttpClient.Builder()
            val log = HttpLoggingInterceptor()
            log.level = HttpLoggingInterceptor.Level.BODY
            build.addInterceptor(log)
            build.addInterceptor(AuthInterceptor())

            build.connectTimeout(30, TimeUnit.SECONDS)
            build.readTimeout(30, TimeUnit.SECONDS)
            build.writeTimeout(30, TimeUnit.SECONDS)
            return build.build()
        }

    }
}