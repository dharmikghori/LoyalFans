package com.calendar.loyalfans.model

import com.calendar.loyalfans.model.response.BaseResponse


open class FansData : BaseResponse() {
    lateinit var data: FansData
    var otp: String = ""
}