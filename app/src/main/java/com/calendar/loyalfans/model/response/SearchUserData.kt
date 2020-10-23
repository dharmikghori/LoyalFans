package com.calendar.loyalfans.model.response

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


open class SearchUserData : BaseResponse() {
    lateinit var data: ArrayList<SearchUsers>
    var search_status: Boolean = false
}

open class SearchUsers : BaseResponse() {
    lateinit var id: String
    lateinit var display_name: String
    lateinit var username: String
    lateinit var profile_img: String

    companion object {
        fun fromJSON(recentSearchStr: String): ArrayList<SearchUsers> {
            val type: Type = object : TypeToken<ArrayList<SearchUsers>>() {}.type
            try {
                return Gson().fromJson(recentSearchStr, type)
            } catch (e: Exception) {
            }
            return ArrayList()
        }

        fun toJSON(searchUsers: ArrayList<SearchUsers>): String {
            try {
                return Gson().toJson(searchUsers)
            } catch (e: Exception) {
            }
            return ""
        }
    }
}


