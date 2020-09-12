package com.calendar.loyalfans.model.request


open class ChangePasswordRequest(
    var current_password: String = "",
    var new_password: String = "",
    var confirm_password: String = "",
) : BaseRequest()