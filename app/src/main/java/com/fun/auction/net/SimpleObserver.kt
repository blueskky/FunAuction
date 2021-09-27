package com.`fun`.auction.net

import android.util.Log
import com.`fun`.auction.model.LoginEvent
import com.`fun`.auction.model.Model
import com.`fun`.auction.util.MyToast
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException


abstract class SimpleObserver<T>() : Observer<Model<T>?> {


    override fun onSubscribe(d: Disposable?) {
    }

    override fun onNext(model: Model<T>?) {
        if (model != null) {
            if (model.success) {
                onSuccess(model.code, model.msg, model.result)
            } else {
                when (model.code) {
                    10001, 9999 -> sendLoginEvent()

                }
                onFail(model.code, model.msg)
            }
        } else {
            onFail(-1, "服务异常")
        }
        onFinish()
    }

    private fun sendLoginEvent() {
        EventBus.getDefault().post(LoginEvent())
    }

    override fun onError(e: Throwable?) {
        val parseException = parseException(e)
        onFail(-1, parseException)
        onFinish()
        Log.d("OkHttp", "xxxxx:${e.toString()}")
    }

    override fun onComplete() {

    }


    abstract fun onSuccess(code: Int, msg: String, data: T?)

    open fun onFail(code: Int, msg: String) {
        MyToast.show(msg)
    }

    open fun onFinish() {}


    open fun parseException(throwable: Throwable?): String {
        var errMsg: String = "未知异常"
        errMsg = if (throwable is SocketTimeoutException) {
            "请求超时,再试一次吧"
        } else if (throwable is ConnectException || throwable is UnknownHostException) {
            "网络异常，请检查网络连接"
        } else {
            "程序出了小差，马上回来"
        }
        return errMsg
    }
}