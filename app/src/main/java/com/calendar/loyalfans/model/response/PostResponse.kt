package com.calendar.loyalfans.model.response


open class PostResponse : BaseResponse() {
    lateinit var data: ArrayList<PostData>
}

open class PostData : BaseResponse() {
    lateinit var id: String
    lateinit var user_id: String
    lateinit var content: String
    lateinit var file_types: String
    lateinit var created_at: String
    lateinit var updated_at: String
    lateinit var display_name: String
    lateinit var username: String
    lateinit var profile_img: String
    lateinit var images: ArrayList<String>
    lateinit var likes: String
    lateinit var comments: String
    lateinit var bookmark: String
    lateinit var is_likes: String
}


