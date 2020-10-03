package com.calendar.loyalfans.model.request


open class NotificationSecurityRequest(
    var type: String = "",
    var sub_type: String = "",
) : BaseRequest()