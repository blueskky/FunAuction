package com.`fun`.auction.presenter

import android.content.Context
import com.`fun`.auction.MyApplication
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.model.Event
import com.`fun`.auction.model.Event.Companion.PEEK_SUCCESS
import com.`fun`.auction.model.PeekIng
import com.`fun`.auction.model.PeekModel
import com.`fun`.auction.model.PrePatModel
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.dialog.DialogCallback
import com.`fun`.auction.ui.dialog.PatSuccessDialog
import com.`fun`.auction.ui.dialog.PeekSuccessDialog
import com.`fun`.auction.ui.dialog.PrePatSuccessDialog
import com.trello.rxlifecycle4.LifecycleProvider
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.FragmentEvent
import org.greenrobot.eventbus.EventBus

class PatPresenter(
    context: Context, view: PatPresenter.PatView,
    fragmentLife: LifecycleProvider<FragmentEvent>? = null,
    activityLife: LifecycleProvider<ActivityEvent>? = null
) {

    var fLifecycle = fragmentLife
    var aLifecycle = activityLife
    var mContext = context
    var iView: PatPresenter.PatView = view


    /**
     * 偷瞄
     */
    fun peek(productId: Int?, name: String?) {
        val map = mapOf("product_id" to productId)
        HttpManager.api?.peek(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(if (aLifecycle != null) aLifecycle?.bindToLifecycle() else fLifecycle?.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<PeekModel>() {
                override fun onSuccess(code: Int, msg: String, data: PeekModel?) {
                    val peek = data?.list
                    peek?.let {
                        if (it.game_over > 0 && it.game_over == PrefercencesManager.getInstance()?.getUserId()) {
                            //拍成功
                            val dialog = PatSuccessDialog(mContext)
                            dialog.show()
                        } else {
                            //偷瞄成功
                            val priceTipDialog = PeekSuccessDialog(mContext, name, it)
                            priceTipDialog.show()
                            priceTipDialog.callback = object : DialogCallback {
                                override fun onConfirm() {
                                    //直接拍下
                                    pat(productId)
                                }
                            }
                            val peeking = PeekIng(productId, it.current_price)
                            MyApplication.addPeek(peeking)
                            iView.onPeekSucc(productId)
                        }
                    }

                    EventBus.getDefault().post(Event(PEEK_SUCCESS))
                }
            })
    }

    /**
     * 直接拍
     */
    fun pat(productId: Int?) {
        val map = mapOf("product_id" to productId)
        HttpManager.api?.pat(map)
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(if (aLifecycle != null) aLifecycle?.bindToLifecycle() else fLifecycle?.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<PrePatModel>() {
                override fun onSuccess(code: Int, msg: String, data: PrePatModel?) {
                    iView.onPrePatSuccess(productId)
                    //预拍成功
                    val dialog = PrePatSuccessDialog(mContext)
                    dialog.show()
                }
            })
    }


    interface PatView {
        fun onPrePatSuccess(productId: Int?)
        fun onPeekSucc(productId: Int?)

    }
}