package com.calendar.loyalfans.model.request


open class AddCommentRequest(
    var post_id: String = "",
    var comment: String = "",
    var comment_id: String = "",
) : BaseRequest()