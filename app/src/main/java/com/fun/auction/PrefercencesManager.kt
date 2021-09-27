package com.`fun`.auction

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

class PrefercencesManager private constructor(context: Context) {

    private val NAME = "WorldFlowers"
    private val Authorization = "Authorization"
    private val ACCOUNT = "account"
    private val PASSWORD = "password"
    private val USER_ID = "userId"
    private val SHOW_POLICY = "show_policy"
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private var edit: SharedPreferences.Editor = sharedPreferences.edit()


    companion object {
        var prefercencesManager: PrefercencesManager? = null

        fun getInstance(): PrefercencesManager? {
            return prefercencesManager
        }

        fun init(context: Context) {
            if (prefercencesManager == null) {
                prefercencesManager = PrefercencesManager(context)
            }
        }
    }


    fun setAuthorization(ahth: String?) {
        edit.putString(Authorization, ahth).apply()
    }

    fun getAuthorization(): String? {
        return sharedPreferences.getString(Authorization, "")
    }


    fun setAccount(account: String?) {
        edit.putString(ACCOUNT, account).apply()
    }

    fun getAccount(): String {
        return sharedPreferences.getString(ACCOUNT, "") ?: ""
    }

    fun setPwd(pwd: String?) {
        edit.putString(PASSWORD, pwd).apply()
    }

    fun getPwd(): String? {
        return sharedPreferences.getString(PASSWORD, "")
    }

    fun setUserId(user_id: Int) {
        edit.putInt(USER_ID, user_id).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(USER_ID, 0)
    }

    fun logout() {
        edit.remove(Authorization)
        edit.remove(ACCOUNT)
        edit.remove(PASSWORD)
        edit.remove(USER_ID)
        edit.apply()
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getAuthorization())
    }

    fun getPolicy(): Boolean {
        return sharedPreferences.getBoolean(SHOW_POLICY, false)
    }

    fun setPolicy() {
        edit.putBoolean(SHOW_POLICY, true).apply()
    }

}