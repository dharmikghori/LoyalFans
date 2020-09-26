package com.calendar.loyalfans.model.request


open class UpdateProfileRequest(
    var display_name: String = "",
    var username: String = "",
    var location: String = "",
    var about: String = "",
    var website: String = "",
    var profile_img: String = "",
    var banner_img: String = "",
) : BaseRequest()