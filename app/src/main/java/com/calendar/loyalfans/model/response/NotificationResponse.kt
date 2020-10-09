package com.calendar.loyalfans.model.response


open class NotificationResponse : BaseResponse() {
    var data: ArrayList<NotificationData> = ArrayList()
}

open class NotificationData {
    var id: String = ""
    var type: String = ""
    var post_id: String = ""
    var type_id: String = ""
    var owner_id: String = ""
    var user_id: String = ""
    var text: String = ""
    var status: String = ""
    var created_at: String = ""
    var is_post: String = ""
    var post_img: String = ""
    var user_name: String = ""
    var user_display_name: String = ""
    var user_profile: String = ""
}

