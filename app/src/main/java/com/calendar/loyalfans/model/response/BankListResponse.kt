package com.calendar.loyalfans.model.response


open class BankListResponse : BaseResponse() {
    lateinit var data: BankListData
    var bank_status: Boolean = false
    var form_status: Boolean = false
}

open class BankListData {
    var first_name: String = ""
    var last_name: String = ""
    var ssn_num: String = ""
    var phone: String = ""
    var street: String = ""
    var city: String = ""
    var state: String = ""
    var status: String = ""
    var country: String = ""
    var birthdate: String = ""
    var doc_type: String = ""
    var exp_date: String = ""
    var zip_code: String = ""
}


