package com.`fun`.auction.net

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

class SchedulersUtils {


    companion object{
        fun <T> applySchedulers(): ObservableTransformer<T, T>? {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
}