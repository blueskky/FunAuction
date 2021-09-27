package com.`fun`.auction.net

import com.`fun`.auction.model.*


import io.reactivex.rxjava3.annotations.Nullable
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {


    @GET("/auth/smsVerify")
    fun smsCode(@Query("account") phone: String): Observable<Model<Any>>


    @GET("/auth/verify")
    fun imgVerify(@Query("account") phone: String): Observable<ResponseBody>


    @POST("/auth/login")
    fun login(@Body map: Map<String, String?>): Observable<Model<LoginAuth>>


    @POST("/auth/refresh")
    fun refresh(@Body map: Map<String, String>): Call<Model<LoginAuth>>


    @GET("/api/")
    fun home(): Observable<Model<List<Session>>>

    @GET("/api/taskInit")
    fun taskList(@Query("match_id") match_id: Int): Observable<Model<TaskModel>>

    @POST("/api/taskAward")
    fun taskAward(@Body map: Map<String, Int>): Observable<Model<TaskModel>>


    @GET("/api/homeRule")
    fun homeRule(@Query("match_id") match_id: Int): Observable<Model<StoreRule>>

    @GET("/api/storeRule")
    fun storeRule(): Observable<Model<StoreRule>>


    @GET("/api/homeMessage")
    fun homeMessage(@Query("match_id") mathId: Int): Observable<Model<List<PrizeMessage>>>


    @POST("/api/homeSingleDraw")
    fun singleDraw(@Body map: Map<String, Int>): Observable<Model<Goods>>


    @POST("/api/homeMoreDraw")
    fun moreDraw(@Body map: Map<String, Int>): Observable<Model<MutableList<Goods>>>


    @GET("/api/taskNum")
    fun taskNum(@Query("match_id") match_id: Int): Observable<Model<Task>>


    @GET("/api/recordLists")
    fun boxRecord(
        @Query("status") status: Int,
        @Nullable @Query("last_id") last_id: Int?
    ): Observable<Model<RecordModel>>


    @GET("/api/storePage")
    fun storePage(@Query("order") order: Int, @Query("page") page: Int): Observable<Model<StoreData>>


    @GET("/api/storeDetails")
    fun goodsdDtail(@Query("product_id") product_id: Int): Observable<Model<GoodsDetail>>


    @GET("/api/minePage")
    fun mine(): Observable<Model<UserModel>>

    @GET("/api/mineRecharge")
    fun chargeList(): Observable<Model<ChargeModel>>


    @POST("/api/goodsOrder")
    fun pat(@Body map: Map<String, Int?>): Observable<Model<PrePatModel>>


    @POST("/api/goodsPeek")
    fun peek(@Body map: Map<String, Int?>): Observable<Model<PeekModel>>


    @POST("/api/addressAdd")
    fun addAddress(@Body map: Map<String, String>): Observable<Model<Any>>

    @POST("/api/addressDelete")
    fun deleteAddress(@Body map: Map<String, String>): Observable<Model<Any>>

    @POST("/api/addressEdit")
    fun editAddress(@Body map: Map<String, String?>): Observable<Model<Any>>

    @POST("/api/addressSetDefault")
    fun defaultAddress(@Body map: Map<String, String>): Observable<Model<Any>>


    @GET("/api/addressLists")
    fun addressList(): Observable<Model<AddressModel>>


    @POST("/order/balancePay")
    fun balancePay(@Body map: Map<String, Int>): Observable<Model<Any>>

    @POST("/order/wechatApp")
    fun wechatPay(@Body map: Map<String, Int>): Observable<Model<PayConfig>>

    @POST("/order/alipayApp")
    fun aliPay(@Body map: Map<String, Int>): Observable<Model<String>>


    @GET("/api/expressList")
    fun logisticsList(
        @Query("order_status") status: Int,
        @Nullable @Query("last_id") last_id: Int?
    ): Observable<Model<LogisticsModel>>


    @GET("/api/expressInfo")
    fun logisticsInfo(@Query("order_id") order_id: String?): Observable<Model<LogisticeModel>>


    @POST("/api/recordSell")
    fun sell(@Body map: Map<String, Int?>): Observable<Model<Any>>

    @POST("/api/recordMail")
    fun enity(@Body map: Map<String, Int?>): Observable<Model<Any>>

    @POST("/api/cardAuth")
    fun auth(@Body map: Map<String, String>): Observable<Model<Any>>

    @GET("/api/authStatus")
    fun authStatus(): Observable<Model<AuthResult>>

    @POST("/api/bindPartner")
    fun bindCode(@Body map: Map<String, String>): Observable<Model<Any>>

    @GET("/api/cashLog")
    fun cashLog(@Query("status") status: Int, @Nullable @Query("last_id") last_id: Int?): Observable<Model<CashModel>>

    @GET("/api/rechargeLog")
    fun chargeLog(@Nullable @Query("last_id") last_id: Int?): Observable<Model<ChargeLogModel>>


    @POST("/api/getCashConfApp")
    fun getCashConfig(): Observable<Model<CashConfig>>

    @POST("/api/setCashConf")
    fun setCashConfig(@Body map: Map<String, String?>): Observable<Model<Any>>

    @POST("/api/setCashConf?t=1")
    fun selectCashConfig(@Body map: Map<String, String?>): Observable<Model<Any>>

    @POST("/api/toCash")
    fun cashOut(@Body map: Map<String, String>): Observable<Model<Any>>


    @GET("/api/recordSellIndex")
    fun sellList(
        @Query("sell_status") status: Int,
        @Nullable @Query("last_id") last_id: Int?
    ): Observable<Model<SellModel>>


    @GET("/api/storeLog")
    fun patLog(
        @Query("box_status") status: Int,
        @Nullable @Query("last_id") last_id: Int?
    ): Observable<Model<PatEndModel>>


    @GET("/api/storeGo")
    fun patIng(@Nullable @Query("last_id") last_id: Int?): Observable<Model<PatModel>>


    @GET("/api/messageList")
    fun message(
        @Query("msg_status") status: Int,
        @Nullable @Query("last_id") last_id: Int?
    ): Observable<Model<MessageModel>>

    @GET("/api/messageInfo")
    fun messageDetail(@Query("msg_id") msg_id: Int): Observable<Model<SystemMsg>>


    @GET("/api/messageNum")
    fun msgNum(): Observable<Model<MessageNum>>


    @GET("/api/recordSellCheck")
    fun resell(@Query("box_id") box_id: Int): Observable<Model<ReSellInfo>>


    @GET("/api/recordDetail")
    fun recordDetail(@Query("box_id") box_id: Int): Observable<Model<RecordDetail>>


    @POST("/api/getPartner")
    fun getPartenr(): Observable<Model<PartnerModel>>

    @POST("/api/forPartner")
    fun applyPartenr(): Observable<Model<Any>>


    @POST("/api/getUserWx")
    fun getWxInfo(@Body map: Map<String, String>): Observable<Model<WxInfo>>


    @POST("/api/verCheck")
    fun versionCheck(@Body map: Map<String, String>): Observable<Model<VersionCheck>>

    @GET("/api/prizeList")
    fun prizeList(@Query("page") page: Int): Observable<Model<WelFareModel>>

    @POST("/api/prizeGet")
    fun getPrize(@Body map: Map<String, Int>): Observable<Model<Any>>
}