package com.calendar.loyalfans.model.response


open class StatementResponse : BaseResponse() {
    var data: ArrayList<StatementData> = ArrayList()
    var balance: String = ""
}

open class StatementData {
    var id: String = ""
    var owner_id: String = ""
    var user_id: String = ""
    var type: String = ""
    var type_id: String = ""
    var charge_id: String = ""
    var txn_id: String = ""
    var card_id: String = ""
    var last4: String = ""
    var brand: String = ""
    var amount: String = ""
    var payment_date: String = ""
    var created: String = ""
    var earning: String = ""
    var display_name: String = ""

}

