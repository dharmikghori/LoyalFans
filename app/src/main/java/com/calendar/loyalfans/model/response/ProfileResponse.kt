package com.calendar.loyalfans.model.response


open class ProfileResponse : BaseResponse() {
    lateinit var data: ProfileData
}

open class ProfileData {
    var display_name = ""
    var username = ""
    var about = ""
    var profile_img = ""
    var favorites = ""
    var followers = ""
    var fans = ""
    var location = ""
    var cover_img = ""
    var website = ""
    var bank_status = "1"
    var card_status = "0"
}