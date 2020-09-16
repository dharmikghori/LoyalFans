package com.calendar.loyalfans.model.request

import com.calendar.loyalfans.utils.Common


open class BaseRequest(
    var user_id: String = Common.getUserId(),
)