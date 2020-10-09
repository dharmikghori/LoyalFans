package com.calendar.loyalfans.model.response

import com.google.gson.annotations.SerializedName


open class LoginResponse : BaseResponse() {
    lateinit var data: LoginResponseData

    @SerializedName("APP-SECRET-KEY")
    var appSecretKey: String = ""
}

open class LoginResponseData {

    var user_id = ""
    var name = ""
    var username = ""
    var profile_img = ""
    var thumb = ""
    var card_status = ""
}