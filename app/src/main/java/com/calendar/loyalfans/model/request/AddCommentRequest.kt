package com.calendar.loyalfans.model.request


open class AddCommentRequest(
    var post_id: String = "",
    var comment: String = "",
) : BaseRequest()