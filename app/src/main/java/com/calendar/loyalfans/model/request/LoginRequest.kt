package com.calendar.loyalfans.model.request


open class LoginRequest(
    var email: String = "",
    var password: String = "",
    var device_id: String = "",
    var device_type: String = "1",
    var fcm_token: String = "",
    var type: String = "",
    var google_id: String = "",
    var fb_id: String = "",
)