package com.calendar.loyalfans.model.request


open class CommentRequest(
    var post_id: String = "",
    var comment_id: String = "",
    var type: String = "",
) : BaseRequest()