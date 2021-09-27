package com.`fun`.auction.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.auction.R
import com.`fun`.auction.ui.adapter.PoiListCheckAdapter
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.mapapi.animation.Animation
import com.baidu.mapapi.animation.Transformation
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_map.*
import java.io.File
import java.io.FileOutputStream


class MapFragment : BaseFragment(), OnGetGeoCoderResultListener {


    var mBaiduMap: BaiduMap? = null

    private var bitmapF: BitmapDescriptor? = null
    private var mMarkerF: Marker? = null
    private var mScreenCenterPoint: Point? = null
    private var mCurrentDirection = 0
    private var myLocationData: MyLocationData? = null
    private var mLocClient: LocationClient? = null
    private var isFirstLoc = true
    private var geoCoder: GeoCoder? = null
    private var mLoadIndex = 0
    private var poiListAdapter: PoiListCheckAdapter? = null


    override fun getLayoutId() = R.layout.fragment_map

    override fun init() {
        mBaiduMap = mapView.map
        mBaiduMap?.isMyLocationEnabled = true


        //地图样式
        val file = File(mContext.externalCacheDir, "map.sty")
        if (!file.exists()) {
            copyFile(mContext, "map.sty", file.path)
        }
        val styleOptions = MapCustomStyleOptions()
        styleOptions.localCustomStylePath(file.path) //本地离线样式文件路径，如果在线方式加载失败，会默认加载本地样式文件。
        styleOptions.customStyleId("75caa75ae1f95a01383100e2b8dc8605") //在线样式文件对应的id。
        mapView.setMapCustomStyle(styleOptions, object : CustomMapStyleCallBack {
            override fun onPreLoadLastCustomMapStyle(p0: String?): Boolean {
                return false
            }

            override fun onCustomMapStyleLoadSuccess(p0: Boolean, p1: String?): Boolean {
                return false
            }

            override fun onCustomMapStyleLoadFailed(p0: Int, p1: String?, p2: String?): Boolean {
                return false
            }
        })

        mBaiduMap?.setOnMapLoadedCallback(object : BaiduMap.OnMapLoadedCallback {
            override fun onMapLoaded() {
                initOverlay()
            }
        })
        mBaiduMap?.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
            override fun onMapStatusChangeStart(status: MapStatus) {}
            override fun onMapStatusChangeStart(status: MapStatus, reason: Int) {}
            override fun onMapStatusChange(status: MapStatus) {}
            override fun onMapStatusChangeFinish(status: MapStatus) {
                if (null == mMarkerF) {
                    return
                }
                mMarkerF?.setAnimation(getTransformationPoint())
                mMarkerF?.startAnimation()


                nearbySearch()
            }
        })


        // 初始化搜索模块，注册事件监听
        geoCoder = GeoCoder.newInstance()
        geoCoder?.setOnGetGeoCodeResultListener(this)

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        poiListAdapter = PoiListCheckAdapter()

        val build = HorizontalDividerItemDecoration.Builder(mContext)
            .color(Color.LTGRAY)
            .sizeResId(R.dimen.px_2)
            .build()
        recyclerView.addItemDecoration(build)
        recyclerView.adapter = poiListAdapter

        poiListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = poiListAdapter?.getItem(position)
                val intent = Intent()
                intent.putExtra("location", item)
                activity?.setResult(RESULT_OK, intent)
                activity?.finish()
            }
        })

        initLocation()
    }

    fun reLocation() {
        isFirstLoc = true
        initLocation()
    }

    private fun initLocation() {
        val locationActivity = activity as LocationActivity
        locationActivity.startLocation()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }


    private fun nearbySearch() {
        val target = mBaiduMap?.mapStatus?.target
        val reverseGeoCodeOption = ReverseGeoCodeOption()
            .location(target) // 设置反地理编码位置坐标
            .newVersion(1) // 设置是否返回新数据 默认值0不返回，1返回
            .radius(500) //  POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
            .pageNum(mLoadIndex)
            .pageSize(10)
        // 发起反地理编码请求，该方法必须在监听之后执行，否则会在某些场景出现拿不到回调结果的情况
        geoCoder?.reverseGeoCode(reverseGeoCodeOption)
    }

    /**
     * 创建平移坐标动画
     */
    private fun getTransformationPoint(): Animation? {
        val centerPoint = mScreenCenterPoint
        if (centerPoint != null) {
            val pointTo = Point(centerPoint.x, centerPoint.y - 100)
            val transformation = Transformation(centerPoint, pointTo, centerPoint)
            transformation.setDuration(400)
            transformation.setRepeatMode(Animation.RepeatMode.RESTART) // 动画重复模式
            transformation.setRepeatCount(1) // 动画重复次数
            transformation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart() {}
                override fun onAnimationEnd() {}
                override fun onAnimationCancel() {}
                override fun onAnimationRepeat() {}
            })
            return transformation
        }
        return null
    }


    /**
     * 初始化Overlay
     */
    fun initOverlay() {
        bitmapF = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark)
        if (null != mBaiduMap?.mapStatus) {
            val latLngF = mBaiduMap?.mapStatus?.target
            mScreenCenterPoint = mBaiduMap?.projection?.toScreenLocation(latLngF)
            val markerOptionsF = MarkerOptions().position(latLngF).icon(bitmapF).perspective(true)
                .fixedScreenPosition(mScreenCenterPoint)
            mMarkerF = mBaiduMap?.addOverlay(markerOptionsF) as Marker
        }
        val mapStatusUpdate = MapStatusUpdateFactory.zoomTo(13.0f)
        mBaiduMap?.setMapStatus(mapStatusUpdate)
    }


    override fun onGetGeoCodeResult(p0: GeoCodeResult?) {

    }

    override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show()
            return
        }

        // 获取周边poi结果
        val poiList = result.poiList
        if (!poiList.isNullOrEmpty()) {
            poiListAdapter?.setNewInstance(poiList)
        } else {
            poiListAdapter?.setNewInstance(mutableListOf())
        }

    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        geoCoder?.destroy()
        mLocClient?.stop()
        mBaiduMap?.isMyLocationEnabled = false
        mapView?.onDestroy()
        mMarkerF?.cancelAnimation()
        bitmapF?.recycle()
    }


    fun onReceiveLocation(location: BDLocation?) {
        if (location == null || mBaiduMap == null) {
            return
        }
        myLocationData = MyLocationData.Builder()
            .accuracy(location.radius) // 设置定位数据的精度信息，单位：米
            .direction(mCurrentDirection.toFloat()) // 此处设置开发者获取到的方向信息，顺时针0-360
            .latitude(location.latitude)
            .longitude(location.longitude)
            .build()

        mBaiduMap?.setMyLocationData(myLocationData)

        if (isFirstLoc) {
            isFirstLoc = false
            val ll = LatLng(location.latitude, location.longitude)
            val builder = MapStatus.Builder()
            builder.target(ll).zoom(18.0f)
            mBaiduMap?.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        }
    }


    fun copyFile(context: Context, assetsName: String, savePath: String): Boolean {
        // assetsPath 为空时即 /assets
        try {
            val input = context.assets.open(assetsName)
            val out = FileOutputStream(File(savePath))
            val buffer = ByteArray(1024)
            var byteCount = 0
            while (input.read(buffer).also { byteCount = it } != -1) { // 循环从输入流读取
                // buffer字节
                out.write(buffer, 0, byteCount) // 将读取的输入流写入到输出流
            }
            out.flush() // 刷新缓冲区
            input.close()
            out.close()
        } catch (e: Exception) {
            return false
        }
        return true
    }

}