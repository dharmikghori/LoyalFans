package com.calendar.loyalfans.model.response

import java.io.Serializable


open class PostResponse : BaseResponse() {
    lateinit var data: ArrayList<PostData>
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
//    var videos: ArrayList<String> = ArrayList()
    var likes: String = "0"
    var comments: String = "0"
    var bookmark: String = ""
    var is_likes: String = ""
}


