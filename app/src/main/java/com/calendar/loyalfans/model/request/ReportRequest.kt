package com.calendar.loyalfans.model.request


open class ReportRequest(
    var other_id: String = "",
    var type: String = "",
    var report_comment: String = "",
) : BaseRequest()