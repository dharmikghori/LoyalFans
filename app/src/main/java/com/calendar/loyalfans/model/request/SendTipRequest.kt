package com.calendar.loyalfans.model.request


open class SendTipRequest(
    var owner_id: String = "",
    var amount: String = "",
    var post_id: String = "",
) : BaseRequest()