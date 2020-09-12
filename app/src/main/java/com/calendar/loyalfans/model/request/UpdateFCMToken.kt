package com.calendar.loyalfans.model.request


open class UpdateFCMToken(
    var fcm_token: String = "",
) : BaseRequest()