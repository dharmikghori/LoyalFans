package com.calendar.loyalfans.model.response


open class FansResponse : BaseResponse() {
    var data: ArrayList<FansData> = ArrayList()
}

open class FansData : BaseResponse() {
    var display_name: String = ""
    var username: String = ""
    var fanid: String = ""
    var end_date: String? = null
    var followingID: String = ""
    var profile_img: String = ""
    var isSelected: Boolean = false
}


