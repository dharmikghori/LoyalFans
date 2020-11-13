package com.calendar.loyalfans.model.response

import java.io.Serializable


open class PostResponse : BaseResponse() {
    var data: ArrayList<PostData> = ArrayList()
}

open class PostData : BaseResponse(), Serializable {
    var id: String = ""
    var user_id: String = ""
    var content: String = ""
    var file_types: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    var display_name: String = ""
    var username: String = ""
    var profile_img: String = ""
    var images: ArrayList<String> = ArrayList()
    var likes: String = "0"
    var comments: String = "0"
    var bookmark: String = ""
    var is_likes: String = ""
    var report_abuse: String = ""
    var suggestions: ArrayList<SuggestionData> = ArrayList()
}

open class SuggestionData {
    var username: String = ""
    var display_name: String = ""
    var profile_img: String = ""
    var cover_img: String = ""
    var business_type: String = ""
    var id: String = ""
    var subscription_plans: ArrayList<SubscriptionPlanData>? = null

}

open class SubscriptionPlanData {
    var months: String = ""
    var amount: String = ""
}


