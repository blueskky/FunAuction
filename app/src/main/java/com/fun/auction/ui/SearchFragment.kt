package com.`fun`.auction.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.ui.adapter.PoiListCheckAdapter
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

    var poiSearch: PoiSearch? = null
    var cityStr: String? = null
    var mLoadIndex: Int = 0

    override fun getLayoutId() = R.layout.fragment_search

    override fun init() {
        recyclerView.setLayoutManager(LinearLayoutManager(mContext))
        var poiListAdapter = PoiListCheckAdapter()
        recyclerView.setAdapter(poiListAdapter)
        poiListAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item: PoiInfo = poiListAdapter.getItem(position)
                val intent = Intent()
                intent.putExtra("location", item)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        })


        //创建poi检索实例
        poiSearch = PoiSearch.newInstance()
        //创建poi监听者
        val poiListener = object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(result: PoiResult) {
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    mLoadIndex = 0
                    Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show()
                    return
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                    // 获取poi结果
                    val mPoiList = result.allPoi
                    poiListAdapter.setNewInstance(mPoiList)
                    return
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    var strInfo = "在"
                    for (cityInfo in result.suggestCityList) {
                        strInfo += cityInfo.city
                        strInfo += ","
                    }
                    strInfo += "找到结果"
                    Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show()
                }
            }

            override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {}
            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult) {}
            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {}
        }
        //设置poi监听者该方法要先于检索方法searchNearby(PoiNearbySearchOption)前调用，否则会在某些场景出现拿不到回调结果的情况
        poiSearch?.setOnGetPoiSearchResultListener(poiListener)
    }

    fun search(key: String) {
        // 发起请求
        if (cityStr != null) {
            val option = PoiCitySearchOption()
                .city(cityStr)
                .keyword(key)
                .pageNum(mLoadIndex) // 分页编号
                .cityLimit(false) //区域数据召回限制为true时，仅召回city对应区域内数据
                .scope(2) //检索结果详细程度：取值为1 或空，则返回基本信息；取值为2，返回检索POI详细信息

            poiSearch?.searchInCity(option)
        }
    }

    fun setCity(city: String?) {
        cityStr = city
    }

    fun clearSearch() {

    }
}