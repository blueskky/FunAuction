package com.`fun`.auction.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.`fun`.auction.R
import com.`fun`.auction.util.LocationHelper
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.geocode.*
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.common_title.*

class LocationActivity : BaseActivity(), View.OnClickListener {

    val mapFragment by lazy { MapFragment() }
    val searchFragment by lazy { SearchFragment() }

    companion object {
        fun startForResult(context: Activity?, code: Int) {
            context?.startActivityForResult(Intent(context, LocationActivity::class.java), code)
        }
    }

    override fun getLayoutId() = R.layout.activity_location

    override fun init() {
        initTitle(iv_back, tv_title, "选择地址")

        viewpager.apply {
            adapter = object : FragmentStateAdapter(this@LocationActivity) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> mapFragment
                        else -> searchFragment
                    }
                }
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tv_switch.visibility = if (position == 1) VISIBLE else GONE
                    iv_location.visibility = if (position == 0) VISIBLE else GONE
                }
            })
            isUserInputEnabled = false
        }

        et_adress.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                val currentItem = viewpager.currentItem
                if (currentItem != 1) {
                    viewpager.setCurrentItem(1, false)
                }
            }
        }

        tv_switch.setOnClickListener(this)
        iv_location.setOnClickListener(this)

        et_adress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val key = s.toString()
                if (!key.isNullOrEmpty()) {
                    searchFragment.search(key)
                } else {
                    searchFragment.clearSearch()
                }
            }
        })

        startLocation()
    }


    fun startLocation() {
        LocationHelper.getInstance()
            .initLocationOption(mContext, object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation?) {
                    val address = location?.address
                    val city = address?.city
                    tv_city.text = city

                    mapFragment.onReceiveLocation(location)
                    searchFragment.setCity(city)
                }
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        LocationHelper.getInstance().stop()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_location -> {
                mapFragment.reLocation()
            }
            R.id.tv_switch
            -> {
                viewpager.setCurrentItem(0, false)
                et_adress.clearFocus()
            }
        }
    }

}