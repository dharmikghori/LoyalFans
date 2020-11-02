package com.calendar.loyalfans.model.request


open class BlockUnblockRequest(
    var following_id: String = "",
    var is_block: String = "",
) : BaseRequest()