package com.calendar.loyalfans.model.request


open class PaidSubscriptionRequest(
    var plan_id: String = "",
    var month: String = "",
    var price: String = "",
    var owner_id: String = "",
    var subscription_id: String = "",
) : BaseRequest()