package com.`fun`.auction.ui

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.`fun`.auction.R
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.common_title.*

class WebViewActivity : BaseActivity() {


    val title by lazy {
        intent.getStringExtra("title") ?: ""
    }
    val url by lazy {
        intent.getStringExtra("url") ?: ""
    }

    companion object {
        fun start(context: Context?, title: String, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_web_view

    override fun init() {
        initTitle(iv_back, tv_title, title)

        webView.apply {
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.setSupportZoom(false)

//            settings.useWideViewPort = true
//            settings.loadWithOverviewMode = true

            webViewClient = object : WebViewClient() {

            }
        }

        webView.loadUrl(url)
    }
}