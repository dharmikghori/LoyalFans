package com.calendar.loyalfans.retrofit

import com.calendar.loyalfans.model.response.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIServices {
    companion object {
        const val MAIN_URL = "http://www.loyalfansonly.com/"
        const val W9_FORM_WEB_URL = MAIN_URL + "welcome/w9form/"
        const val W9_FORM_DOWNLOAD_URL = MAIN_URL + "wform/"
        const val AUTH_TOKEN = "eyJ0eXA1iOi0JKV1QiL8CJhb5GciTWvLUzI1NiJ9IiRk2YXRh8Ig"
        const val APP_SECRET_KEY = "Mn2fKZG4M1170jDlVn6lOFTN6OE27f6UO99n9QDV"
        const val SERVICE_URL = "http://www.loyalfansonly.com/v1/"
        const val SIGN_IN = "signin"
        const val SIGN_UP = "signup"
        const val FORGOT_PASSWORD = "forgotpassword"
        const val UPDATE_FCM_TOKEN = "auth/updateFcmToken"
        const val CHANGE_PASSWORD = "auth/changepassword"
        const val FAVORITE_POST = "auth/favoritepost"
        const val LIKE_POST = "auth/likepost"
        const val SEARCH_USER = "auth/searchuser"
        const val NEW_POST = "auth/newpost"
        const val CREATE_PPV = "auth/createppv"
        const val POST_LIST = "auth/postlist"
        const val PROFILE_POST = "auth/getpostdata"
        const val UPDATE_POST = "auth/updatepost"
        const val DELETE_POST = "auth/deletepost"
        const val GET_PROFILE = "auth/getprofile"
        const val GET_EDIT_PROFILE = "auth/editprofile"
        const val UPDATE_PROFILE = "auth/updateprofile"
        const val SUBSCRIPTION_PLAN = "auth/subscriptionplan"
        const val GET_FANS = "auth/fanslist"
        const val GET_FOLLOWING = "auth/getfollowings"
        const val GET_FAVORITE = "auth/favoritelist"
        const val PPV_HISTORY = "auth/ppvhistory"
        const val GET_COMMENTS = "auth/getcomments"
        const val ADD_COMMENTS = "auth/addcomment"
        const val LIKE_COMMENT = "auth/likecomment"
        const val SEND_TIP = "auth/sendtip"
        const val FOLLOW_USER = "auth/followuser"
        const val ADD_CARD = "auth/addcard"
        const val PAID_SUBSCRIPTION = "auth/createsubscription"
        const val CANCEL_PAID_SUBSCRIPTION = "auth/cancelsubscription"
        const val STATE_LIST = "auth/statelist"
        const val CITY_LIST = "auth/citylist"
        const val ADD_BANK = "auth/addbank"
        const val BANK_LIST = "auth/banklist"
        const val W9_FORM_STATUS = "auth/w9formstatus"
        const val BANK_TRANSFER = "auth/banktransfer"
        const val EMAIL_NOTIFICATION = "auth/emailnotification"
        const val FAVORITE_PROFILE = "auth/favoriteprofile"
        const val NOTIFICATION_LIST = "auth/notificationlist"
        const val STATEMENTS = "auth/statements"
        const val WITHDRAWAL_REQ = "auth/withdrawalreq"
        const val PAYMENT_HISTORY = "auth/paymenthistory"
        const val PAY_PPV_POST = "auth/paytoppvpost"
        const val SEEN_PPV = "auth/seenppv"
        const val PPV_SENT_USER = "auth/ppvsendtouser"
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
        @Field("name") name: String,
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
//    Call using HTTP client, getting error while multiple images
//    @Multipart
//    @POST(NEW_POST)
//    fun newPost(
//        @Part("user_id") user_id: RequestBody,
//        @Part("content") content: RequestBody,
//        @Part files: List<MultipartBody.Part>,
//    ): Call<BaseResponse>


    @FormUrlEncoded
    @POST(POST_LIST)
    fun postList(
        @Field("user_id") user_id: String,
        @Field("offset") offset: String,
        @Field("limit") limit: String,
    ): Call<PostResponse>

    @FormUrlEncoded
    @POST(PROFILE_POST)
    fun profilePost(
        @Field("user_id") user_id: String,
        @Field("offset") offset: String,
        @Field("limit") limit: String,
        @Field("profile_id") profile_id: String,
        @Field("type") type: String,
    ): Call<PostResponse>

    @FormUrlEncoded
    @POST(UPDATE_POST)
    fun updatePost(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
        @Field("content") content: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(DELETE_POST)
    fun deletePost(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(GET_PROFILE)
    fun getProfile(
        @Field("user_id") user_id: String,
        @Field("profile_id") profile_id: String,
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @POST(GET_EDIT_PROFILE)
    fun getEditProfile(
        @Field("user_id") user_id: String,
        @Field("profile_id") profile_id: String,
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @POST(UPDATE_PROFILE)
    fun updateProfile(
        @Field("user_id") user_id: String,
        @Field("display_name") display_name: String,
        @Field("location") location: String,
        @Field("about") about: String,
        @Field("website") website: String,
        @Field("profile_img") profile_img: String,
        @Field("banner_img") banner_img: String,
        @Field("username") username: String,
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @POST(SUBSCRIPTION_PLAN)
    fun setSubscriptionPlan(
        @Field("user_id") user_id: String,
        @Field("subscription_plans") subscription_plans: String,
    ): Call<SubscriptionResponse>

    @FormUrlEncoded
    @POST(SUBSCRIPTION_PLAN)
    fun getSubscriptionPlan(
        @Field("user_id") user_id: String,
    ): Call<SubscriptionResponse>

    @FormUrlEncoded
    @POST(GET_FANS)
    fun getFansByType(
        @Field("user_id") user_id: String,
        @Field("profile_id") profile_id: String,
        @Field("type") type: String,
    ): Call<FansResponse>

    @FormUrlEncoded
    @POST(GET_FOLLOWING)
    fun getFollowingByType(
        @Field("user_id") user_id: String,
        @Field("profile_id") profile_id: String,
        @Field("type") type: String,
    ): Call<FansResponse>


    @FormUrlEncoded
    @POST(GET_FAVORITE)
    fun getFavorite(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
    ): Call<FavouriteResponse>


    @FormUrlEncoded
    @POST(PPV_HISTORY)
    fun ppvHistory(
        @Field("user_id") user_id: String,
        @Field("profile_id") profile_id: String,
    ): Call<PpvHistoryResponse>

    @FormUrlEncoded
    @POST(GET_COMMENTS)
    fun getCommentsByPostId(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
    ): Call<CommentResponse>

    @FormUrlEncoded
    @POST(ADD_COMMENTS)
    fun addComments(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
        @Field("comment") comment: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(ADD_COMMENTS)
    fun replyComment(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
        @Field("comment") comment: String,
        @Field("comment_id") commentId: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(LIKE_COMMENT)
    fun likeComment(
        @Field("user_id") user_id: String,
        @Field("post_id") post_id: String,
        @Field("comment_id") comment: String,
        @Field("type") type: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(SEND_TIP)
    fun sendTip(
        @Field("user_id") user_id: String,
        @Field("owner_id") owner_id: String,
        @Field("amount") amount: String,
        @Field("post_id") post_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(FOLLOW_USER)
    fun followUser(
        @Field("user_id") user_id: String,
        @Field("profile_id") profile_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(ADD_CARD)
    fun addCard(
        @Field("user_id") user_id: String,
        @Field("street") street: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("zip") zip: String,
        @Field("country") country: String,
        @Field("name") name: String,
        @Field("card_num") card_num: String,
        @Field("exp_year") exp_year: String,
        @Field("exp_month") exp_month: String,
        @Field("cvv") cvv: String,
    ): Call<BaseResponse>


    @FormUrlEncoded
    @POST(PAID_SUBSCRIPTION)
    fun followPaidUser(
        @Field("user_id") user_id: String,
        @Field("plan_id") plan_id: String,
        @Field("month") month: String,
        @Field("price") price: String,
        @Field("owner_id") owner_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(CANCEL_PAID_SUBSCRIPTION)
    fun unFollowPaidUser(
        @Field("user_id") user_id: String,
        @Field("owner_id") owner_id: String,
        @Field("subscription_id") subscription_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(STATE_LIST)
    fun stateList(
        @Field("user_id") user_id: String,
    ): Call<StateCityResponse>

    @FormUrlEncoded
    @POST(CITY_LIST)
    fun cityList(
        @Field("user_id") user_id: String,
        @Field("state_id") state_id: String,
    ): Call<StateCityResponse>

    @FormUrlEncoded
    @POST(BANK_LIST)
    fun bankList(
        @Field("user_id") user_id: String,
    ): Call<BankListResponse>

    @FormUrlEncoded
    @POST(W9_FORM_STATUS)
    fun w9FormStatus(
        @Field("user_id") user_id: String,
    ): Call<BankListResponse>

    @FormUrlEncoded
    @POST(BANK_TRANSFER)
    fun bankTransfer(
        @Field("user_id") user_id: String,
        @Field("route_num") route_num: String,
        @Field("account_num") account_num: String,
        @Field("account_type") account_type: String,
        @Field("country") country: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("business_name") business_name: String,
        @Field("email") email: String,
    ): Call<BankListResponse>

    @FormUrlEncoded
    @POST(EMAIL_NOTIFICATION)
    fun notificationSetting(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
    ): Call<NotificationSecurityResponse>

    @FormUrlEncoded
    @POST(EMAIL_NOTIFICATION)
    fun notificationSetting(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
        @Field("sub_type") sub_type: String,
    ): Call<NotificationSecurityResponse>

    @FormUrlEncoded
    @POST(FAVORITE_PROFILE)
    fun favoriteProfile(
        @Field("user_id") user_id: String,
        @Field("owner_id") owner_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(NOTIFICATION_LIST)
    fun notificationList(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
    ): Call<NotificationResponse>

    @FormUrlEncoded
    @POST(STATEMENTS)
    fun getStatement(
        @Field("user_id") user_id: String,
        @Field("type") type: String,
    ): Call<StatementResponse>

    @FormUrlEncoded
    @POST(WITHDRAWAL_REQ)
    fun withdrawalRequest(
        @Field("user_id") user_id: String,
        @Field("amount") amount: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(PAYMENT_HISTORY)
    fun paymentHistory(
        @Field("user_id") user_id: String,
    ): Call<StatementResponse>

    @FormUrlEncoded
    @POST(PAY_PPV_POST)
    fun payPPVPost(
        @Field("user_id") user_id: String,
        @Field("ppv_id") ppv_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(SEEN_PPV)
    fun seenPPV(
        @Field("user_id") user_id: String,
        @Field("ppv_id") ppv_id: String,
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(PPV_SENT_USER)
    fun ppvSendUser(
        @Field("user_id") user_id: String,
        @Field("touser_id") touser_id: String,
        @Field("ppv_id") ppv_id: String,
    ): Call<BaseResponse>

}