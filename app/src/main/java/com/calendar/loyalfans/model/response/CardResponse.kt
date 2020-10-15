package com.calendar.loyalfans.model.response


open class CardResponse : BaseResponse() {
    lateinit var data: CardData
    var card_status: Boolean = false
}

open class CardData {
    var name: String = ""
    var last4: String = ""
    var payment_method: String = ""
}

