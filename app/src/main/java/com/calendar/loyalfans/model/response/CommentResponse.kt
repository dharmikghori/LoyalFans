package com.calendar.loyalfans.model.response


open class CommentResponse : BaseResponse() {
    lateinit var data: ArrayList<CommentData>
}

open class CommentData {
    var id: String = ""
    var user_id: String = ""
    var comment: String = ""
    var is_like: String = ""
    var likes: String = ""
    var display_name: String = ""
    var profile_img: String = ""

}


