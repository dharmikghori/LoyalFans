package com.calendar.loyalfans.model.response


open class NotificationSecurityResponse : BaseResponse() {
    lateinit var data: NotificationSecurityData
}

open class NotificationSecurityData {
    var activity_status: String = ""
    var sub_offers: String = ""
    var site_noti_about: String = ""
    var noti_email: String = ""
    var site_noti_comm: String = ""
    var site_noti_likes: String = ""
    var site_noti_tips: String = ""
}

