package com.calendar.loyalfans.model.response


open class SubscriptionResponse : BaseResponse() {
    lateinit var data: ArrayList<SubscriptionData>
}

open class SubscriptionData {
    var id: String = ""
    var user_id: String = ""
    var months: String = ""
    var amount: String = ""
    var status: String = ""
}


