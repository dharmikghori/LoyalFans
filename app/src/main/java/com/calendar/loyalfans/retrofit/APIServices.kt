package com.calendar.loyalfans.retrofit

import com.calendar.loyalfans.model.response.BaseResponse
import com.calendar.loyalfans.model.response.LoginResponse
import com.calendar.loyalfans.model.response.PostResponse
import com.calendar.loyalfans.model.response.SearchUserData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIServices {
    companion object {
        const val AUTH_TOKEN = "eyJ0eXA1iOi0JKV1QiL8CJhb5GciTWvLUzI1NiJ9IiRk2YXRh8Ig"
        const val APP_SECRET_KEY = "Mn2fKZG4M1170jDlVn6lOFTN6OE27f6UO99n9QDV"
        const val SERVICE_URL = "https://loyalfansonly.com/v1/"
        const val SIGN_IN = "signin"
        const val SIGN_UP = "signup"
        const val FORGOT_PASSWORD = "forgotpassword"
        const val UPDATE_FCM_TOKEN = "auth/updateFcmToken"
        const val CHANGE_PASSWORD = "auth/changepassword"
        const val FAVORITE_POST = "auth/favoritepost"
        const val LIKE_POST = "auth/likepost"
        const val SEARCH_USER = "auth/searchuser"
        const val NEW_POST = "auth/newpost"
        const val POST_LIST = "auth/postlist"
    }

    @FormUrlEncoded
    @POST(SIGN_IN)
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String,
        @Field("device_type") device_type: String,
        @Field("fcm_token") fcm_token: String,
        @Field("type") type: String,
        @Field("google_id") google_id: String,
        @Field("fb_id") fb_id: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST(SIGN_UP)
    fun registration(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String,
        @Field("device_type") device_type: String,
        @Field("fcm_token") fcm_token: String,
        @Field("type") type: String,
        @Field("google_id") google_id: String,
        @Field("fb_id") fb_id: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD)
    fun forgotPassword(
        @Field("email") email: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(UPDATE_FCM_TOKEN)
    fun updateFcmToken(
        @Field("user_id") user_id: String,
        @Field("fcm_token") fcm_token: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    fun changePassword(
        @Field("user_id") user_id: String,
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Field("confirm_password") confirm_password: String,
    ): Call<BaseResponse>


    @FormUrlEncoded
    @POST(LIKE_POST)
    fun likePost(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(FAVORITE_POST)
    fun favoritePost(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
    ): Call<BaseResponse>


    @FormUrlEncoded
    @POST(SEARCH_USER)
    fun searchUser(
        @Field("user_id") user_id: String,
        @Field("text") searchName: String,
    ): Call<SearchUserData>

    //Call using HTTP client, getting error while multiple images
    @Multipart
    @POST(NEW_POST)
    fun newPost(
        @Part("user_id") user_id: RequestBody,
        @Part("content") content: RequestBody,
        @Part files: List<MultipartBody.Part>,
    ): Call<BaseResponse>


    @FormUrlEncoded
    @POST(POST_LIST)
    fun postList(
        @Field("user_id") user_id: String,
        @Field("offset") offset: String,
        @Field("limit") limit: String,
    ): Call<PostResponse>


}