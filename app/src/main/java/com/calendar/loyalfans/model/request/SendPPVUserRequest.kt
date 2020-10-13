package com.calendar.loyalfans.model.request


open class SendPPVUserRequest(
    val touser_id: String,
    val ppv_id: String,
) : BaseRequest()