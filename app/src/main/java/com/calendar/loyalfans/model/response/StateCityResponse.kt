package com.calendar.loyalfans.model.response


open class StateCityResponse : BaseResponse() {
    lateinit var data: ArrayList<StateCityData>
}

open class StateCityData {
    var id: String = ""
    var name: String = ""
    var state_id: String = ""
    var status: String = ""
}

