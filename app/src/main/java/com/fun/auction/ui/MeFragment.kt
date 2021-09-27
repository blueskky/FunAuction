package com.`fun`.auction.ui


import android.graphics.Color
import android.view.View
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.`fun`.auction.MyApplication
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.R
import com.`fun`.auction.model.*
import com.`fun`.auction.net.HttpManager
import com.`fun`.auction.net.SchedulersUtils
import com.`fun`.auction.net.SimpleObserver
import com.`fun`.auction.ui.adapter.MeAdapter
import com.`fun`.auction.ui.dialog.InvitationDialog
import com.`fun`.auction.ui.dialog.TipDialog
import com.`fun`.auction.util.MyToast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MeFragment : BaseFragment(), View.OnClickListener {


    var user: User? = null
    var adapter: MeAdapter? = null

    companion object {
        fun newInstance(): Fragment {
            return MeFragment()
        }
    }


    override fun getLayoutId() = R.layout.fragment_me

    override fun init() {
        EventBus.getDefault().register(this)
        ll_login.setOnClickListener(this)
        iv_setting.setOnClickListener(this)
        tv_account_record.setOnClickListener(this)
        btn_charge.setOnClickListener(this)
        tv_transfer.setOnClickListener(this)
        tv_bind.setOnClickListener(this)
        ll_service.setOnClickListener(this)

        recyclerview.layoutManager = GridLayoutManager(mContext, 4)
        adapter = MeAdapter(arrayOfNulls<Any>(8).toMutableList())
        recyclerview.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                when (position) {
                    0 -> BoxRecordActivity.start(mContext)
                    1 -> PatRecordActivity.start(mContext, 0)
                    2 -> SellCenterActivity.start(mContext)
                    3 -> LogisticsCenterActivity.start(mContext)
                    4 -> VipWelfareActivity.start(mContext)
                    5 -> showDialog()
                    6 -> PartnerActivity.start(mContext)
                    7 -> SystemMsgActivity.start(mContext)

                }
            }
        })

        refreshLayout.setOnRefreshListener {
            getMeInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        getMeInfo()
    }


    private fun getMeInfo() {
        HttpManager.api?.mine()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<UserModel>() {
                override fun onSuccess(code: Int, msg: String, data: UserModel?) {
                    user = data?.user
                    user?.let {
                        account.text = "个人账户：${it.phone}"
                        tvPoint.text = "积分：${it.experience}"
                        tv_coin.text = "${it.score}"

                        if (it.bind_code == 0) {
                            tv_bind.text = "您未绑定推广码，去绑定 >"
                            tv_bind.setBackgroundResource(R.drawable.bind_code_shape)
                        } else {
                            tv_bind.text = "你绑定的推广码：${it.bind_code}"
                            tv_bind.setBackgroundResource(R.drawable.bind_code)
                            tv_bind.setTextColor(Color.parseColor("#ffffff"))
                        }
                    }
                }

                override fun onFail(code: Int, msg: String) {

                }
            })

        HttpManager.api?.msgNum()
            ?.compose(SchedulersUtils.applySchedulers())
            ?.compose(this.bindToLifecycle())
            ?.subscribe(object : SimpleObserver<MessageNum>() {
                override fun onSuccess(code: Int, msg: String, data: MessageNum?) {
                    adapter?.haveMessage = data?.total ?: 0
                }

                override fun onFinish() {
                    refreshLayout.finishRefresh()
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_login -> {
                val login = PrefercencesManager.getInstance()?.isLogin()
                if (login == null || !login) {
                    EventBus.getDefault().post(LoginEvent())
                }
            }
            R.id.iv_setting -> SettingActivity.start(mContext)

            R.id.tv_bind -> {
                if (user?.bind_code == 0) {
                    BindCodeActivity.start(mContext) //绑码
                }
            }
            R.id.tv_account_record -> ChargeLogActivity.start(mContext) // AccountRecordActivity.start(mContext)//流水记录
            R.id.btn_charge -> ChargeActivity.start(mContext) //充值
            R.id.tv_transfer -> "" //转让

            R.id.ll_service -> TipDialog(mContext).show()

        }
    }


    private fun showDialog() {
        user?.let {
            val dialog = InvitationDialog(mContext)
            dialog.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun chargeRefresh(event: Event) {
        getMeInfo()
    }

}