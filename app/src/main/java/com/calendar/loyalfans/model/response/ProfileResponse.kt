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
    var banner_img = ""
    var website = ""
    var tofollow = "0"
    var business_type = ""
    var isfavorite = ""
    var bank_status = "1"
    var card_status = "0"
    var subscription_plans: ArrayList<ProfileSubscriptionData>? = null
}

open class ProfileSubscriptionData {
    var profile_id = ""
    var months = ""
    var amount = ""
    var id = ""
    var is_subscribe = ""
    var subscription_id = ""
}