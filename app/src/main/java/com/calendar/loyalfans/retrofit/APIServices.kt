package com.calendar.loyalfans.retrofit

import com.calendar.loyalfans.model.response.BaseResponse
import com.calendar.loyalfans.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIServices {
    companion object {
        const val AUTH_TOKEN = "eyJ0eXA1iOi0JKV1QiL8CJhb5GciTWvLUzI1NiJ9IiRk2YXRh8Ig"
        const val APP_SECRET_KEY = "Mn2fKZG4M1170jDlVn6lOFTN6OE27f6UO99n9QDV"
        const val SERVICE_URL = "https://loyalfansonly.com/v1/"
        const val SIGN_IN = "signin"
        const val SIGN_UP = "signup"
        const val FORGOT_PASSWORD = "forgotpassword"
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


}