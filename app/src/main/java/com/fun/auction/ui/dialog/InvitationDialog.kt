package com.`fun`.auction.ui.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import com.`fun`.auction.BuildConfig
import com.`fun`.auction.PrefercencesManager
import com.`fun`.auction.R
import com.`fun`.auction.util.CodeUtils
import com.`fun`.auction.util.MyToast
import com.`fun`.auction.util.ViewShotUtil

import kotlinx.android.synthetic.main.dialog_goods_invita.*
import java.io.File

class InvitationDialog(context: Context) : BaseDialog(context), View.OnClickListener {


    val host = "http://www.qupaipaia.com"
    val shareUrl by lazy {
        "${BuildConfig.SHARE_HOST}?invite_id=${PrefercencesManager.getInstance()?.getUserId()}"
    }

    override fun getLayoutId() = R.layout.dialog_goods_invita

    override fun init() {
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        tv_copy.setOnClickListener(this)
        iv_close.setOnClickListener {
            onBackPressed()
        }
        root.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                captureView()
                return false
            }
        })

        val logo = BitmapFactory.decodeResource(context.resources, R.mipmap.icon_launcher)
        val createQRCode = CodeUtils.createQRCode(shareUrl, 400, logo)
        iv_code.setImageBitmap(createQRCode)
    }

    private fun captureView() {
        val cacheDir =
            Environment.getExternalStorageDirectory().path + File.separatorChar + "DCIM" + File.separatorChar + "Screenshots"
        val viewShot = ViewShotUtil.viewShot(ll_card)
        if (viewShot != null) {
            val name = "fun_${System.currentTimeMillis()}.png"
            val file = File(cacheDir, name)
            ViewShotUtil.saveBitmap(viewShot, file.path)
            ViewShotUtil.insertMedia(context, file.path)
            MyToast.show("保存成功")
        }
    }

    override fun onClick(v: View?) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("Label", shareUrl)
        clipboardManager.setPrimaryClip(mClipData)
        MyToast.show("已复制")
    }

}