package com.calendar.loyalfans.model.request


open class RegistrationRequest(
    var email: String = "",
    var name: String = "",
    var password: String = "",
    var device_id: String = "",
    var device_type: String = "",
    var fcm_token: String = "",
    var type: String = "",
    var google_id: String = "",
    var fb_id: String = "",
)