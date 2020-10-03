package com.calendar.loyalfans.model.request


open class BankTransferRequest(
    val route_num: String,
    val account_num: String,
    val account_type: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val business_name: String,
    val email: String,
) : BaseRequest()