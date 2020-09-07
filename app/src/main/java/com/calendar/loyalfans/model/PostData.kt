package com.calendar.loyalfans.model

import com.calendar.loyalfans.model.response.BaseResponse


open class PostData : BaseResponse() {
    lateinit var data: PostData
    var otp: String = ""
}