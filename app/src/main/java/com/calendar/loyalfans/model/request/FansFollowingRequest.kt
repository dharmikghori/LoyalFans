package com.calendar.loyalfans.model.request


open class FansFollowingRequest(
    var type: String = "",
    var profile_id: String = "",
) : BaseRequest()