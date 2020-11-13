package com.calendar.loyalfans.model.response


open class StateCityResponse : BaseResponse() {
    lateinit var data: ArrayList<StateCityData>
}

open class StateCityData {
    var id: String = ""
    var name: String = ""
    var code: String = ""
    var status: String = ""
}

