package com.calendar.loyalfans.model.request


open class PostListRequest(
    var offset: Int = 0,
    var limit: Int = 0,
) : BaseRequest()