package com.calendar.loyalfans.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.calendar.loyalfans.model.response.LoginResponse
import com.calendar.loyalfans.model.response.SearchUsers
import com.calendar.loyalfans.retrofit.APIServices
import com.google.gson.Gson

class SPHelper(mContext: Context) {


    private val defString: String = ""
    private val sp: SharedPreferences

    init {
        sp = SharedPreferences(mContext)
    }


    operator fun get(key: String, defValue: Boolean): Boolean {
        return sp.read(key, defValue)
    }

    fun getString(key: String): String? {
        return defString.let { sp.read(key, it) }
    }

    operator fun get(key: String, defValue: String): String? {
        return sp.read(key, defValue)
    }


    operator fun get(key: String, defValue: Int): Int {
        return sp.read(key, defValue)
    }


    operator fun get(key: String, defValue: Long): Long {
        return sp.read(key, defValue)
    }


    operator fun get(key: String, defValue: Float): Float {
        return sp.read(key, defValue)
    }

    operator fun get(key: String): Bitmap? {
        val bitmap: Bitmap?
        val previouslyEncodedImage = sp.read(key, "")
        bitmap = if (!previouslyEncodedImage!!.equals("", ignoreCase = true)) {
            val b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(b, 0, b.size)
        } else {
            null
        }
        return bitmap
    }

    fun getLoginData(): LoginResponse? {
        val json = sp.read(RequestParams.USER_LOGIN_DATA, "")
        return try {
            Gson().fromJson(json, LoginResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    //  Save login object
    fun saveLoginData(userLoginData: LoginResponse) {
        val json = Gson().toJson(userLoginData)
        sp.save(RequestParams.USER_LOGIN_DATA, json)
    }

    fun saveRecentSearch(searchUserData: SearchUsers) {
        val recentSearch = getRecentSearch()
        val alreadyAddedOrNot = recentSearch.filter { it.username == searchUserData.username }
        if (alreadyAddedOrNot.isEmpty()) {
            recentSearch.add(searchUserData)
        }
        val toJSON = SearchUsers.toJSON(recentSearch)
        sp.save(RequestParams.RECENT_SEARCH_USER_DATA + Common.getUserId(), toJSON)
    }

    fun removeRecentSearch(searchUserData: SearchUsers) {
        val recentSearch = getRecentSearch()
        val tempList = recentSearch
        for (i in 0..tempList.size) {
            val userData = tempList[i]
            if (userData.username.equals(searchUserData.username)) {
                recentSearch.remove(userData)
                break
            }
        }
        val toJSON = SearchUsers.toJSON(recentSearch)
        sp.save(RequestParams.RECENT_SEARCH_USER_DATA + Common.getUserId(), toJSON)
    }

    fun getRecentSearch(): ArrayList<SearchUsers> {
        var searchUsers = ArrayList<SearchUsers>()
        val strJson = get(RequestParams.RECENT_SEARCH_USER_DATA + Common.getUserId(), "")
        strJson?.let { searchUsers = SearchUsers.fromJSON(it) }
        return searchUsers
    }

    fun clearLoginSession() {
        sp.clearSession()
    }

    fun getLoginAppSecretKey(): String {
        val loginData = getLoginData()
        if (loginData != null) {
            return loginData.appSecretKey
        }
        return APIServices.APP_SECRET_KEY
    }

    private class SharedPreferences//  Default constructor
    constructor(private val mContext: Context) {
        val mPREFERENCES = "LoyalFansPref"

        fun clearSession() {
            val editor = mContext.getSharedPreferences(mPREFERENCES, 0).edit()
            editor.clear()
            editor.apply()
        }


        //  Save String value
        fun save(key: String, value: String) {
            val editor = mContext.getSharedPreferences(mPREFERENCES, 0).edit()
            editor.putString(key, value)
            editor.apply()
        }

        //  Read String value
        fun read(key: String, defValue: String): String? {
            return mContext.getSharedPreferences(mPREFERENCES, 0).getString(
                key, defValue
            )
        }

        //  Read Integer value
        fun read(key: String, defValue: Int): Int {
            return mContext.getSharedPreferences(mPREFERENCES, 0).getInt(
                key, defValue
            )
        }

        //  Read Long value
        fun read(key: String, defValue: Long): Long {
            return mContext.getSharedPreferences(mPREFERENCES, 0).getLong(
                key, defValue
            )
        }

        //  Read Float value
        fun read(key: String, defValue: Float): Float {
            return mContext.getSharedPreferences(mPREFERENCES, 0).getFloat(
                key, defValue
            )
        }

        //  Read Boolean value
        fun read(key: String, defValue: Boolean): Boolean {
            return mContext.getSharedPreferences(mPREFERENCES, 0).getBoolean(
                key, defValue
            )
        }

    }

}
