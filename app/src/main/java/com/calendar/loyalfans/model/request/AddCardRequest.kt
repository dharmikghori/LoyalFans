package com.calendar.loyalfans.model.request


open class AddCardRequest(
    val street: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String,
    val name: String,
    val card_num: String,
    val exp_year: String,
    val exp_month: String,
    val cvv: String,
) : BaseRequest()