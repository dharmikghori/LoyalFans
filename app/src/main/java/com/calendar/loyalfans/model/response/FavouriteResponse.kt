package com.calendar.loyalfans.model.response


open class FavouriteResponse : BaseResponse() {
    lateinit var data: ArrayList<FavouriteData>
}

open class FavouriteData : BaseResponse() {
    var display_name: String = ""
    var username: String = ""
    var profile_img: String = ""
    var id: String = ""
    var owner_id: String = ""
    var user_id: String = ""
    var content: String = ""
    var images: ArrayList<String> = ArrayList()
}


