package com.calendar.loyalfans.model.request


open class PostEditRequest(
    var post_id: String = "",
    var content: String = "",
) : BaseRequest()