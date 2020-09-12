package com.calendar.loyalfans.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calendar.loyalfans.BuildConfig
import com.calendar.loyalfans.model.request.*
import com.calendar.loyalfans.model.response.BaseResponse
import com.calendar.loyalfans.model.response.LoginResponse
import com.calendar.loyalfans.model.response.PostResponse
import com.calendar.loyalfans.model.response.SearchUserData
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody


class BaseViewModel : ViewModel() {

    fun login(
        loginRequestData: LoginRequest,
        progressShow: Boolean,
    ): MutableLiveData<LoginResponse> {
        val apiName = APIServices.SIGN_IN
        val loginResponseData: MutableLiveData<LoginResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(loginRequestData, apiName)
        apiServices?.login(
            loginRequestData.email,
            loginRequestData.password,
            loginRequestData.device_id,
            loginRequestData.device_type,
            loginRequestData.fcm_token,
            loginRequestData.type,
            loginRequestData.google_id,
            loginRequestData.fb_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val loginResponse = response as LoginResponse
                printStrJson(loginResponse, apiName)
                loginResponseData.value = loginResponse
            }
        }))
        return loginResponseData
    }


    fun registerNewUser(
        registrationRequest: RegistrationRequest,
        progressShow: Boolean,
    ): MutableLiveData<LoginResponse> {
        val apiName = APIServices.SIGN_UP
        val loginResponseData: MutableLiveData<LoginResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(registrationRequest, apiName)
        apiServices?.registration(
            registrationRequest.email,
            registrationRequest.name,
            registrationRequest.password,
            registrationRequest.device_id,
            registrationRequest.device_type,
            registrationRequest.fcm_token,
            registrationRequest.type,
            registrationRequest.google_id,
            registrationRequest.fb_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val loginResponse = response as LoginResponse
                printStrJson(loginResponse, apiName)
                loginResponseData.value = loginResponse
            }
        }))
        return loginResponseData
    }

    fun forgotPassword(
        forgotPasswordRequest: ForgotPasswordRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.FORGOT_PASSWORD
        val forgotPasswordData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(forgotPasswordRequest, apiName)
        apiServices?.forgotPassword(
            forgotPasswordRequest.email
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val forgotPasswordResponse = response as BaseResponse
                printStrJson(forgotPasswordResponse, apiName)
                forgotPasswordData.value = forgotPasswordResponse
            }
        }))
        return forgotPasswordData
    }

    fun updateFcmToken(
        fcmTokenRequest: UpdateFCMToken,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.UPDATE_FCM_TOKEN
        val fcmTokenData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(fcmTokenRequest, apiName)
        apiServices?.updateFcmToken(
            fcmTokenRequest.user_id,
            fcmTokenRequest.fcm_token
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val updateFCMTokenResponse = response as BaseResponse
                printStrJson(updateFCMTokenResponse, apiName)
                fcmTokenData.value = updateFCMTokenResponse
            }
        }))
        return fcmTokenData
    }


    fun changePassword(
        changePasswordRequest: ChangePasswordRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.CHANGE_PASSWORD
        val changePasswordData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(changePasswordRequest, apiName)
        apiServices?.changePassword(
            changePasswordRequest.user_id,
            changePasswordRequest.current_password,
            changePasswordRequest.new_password,
            changePasswordRequest.confirm_password
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val changePasswordResponse = response as BaseResponse
                printStrJson(changePasswordResponse, apiName)
                changePasswordData.value = changePasswordResponse
            }
        }))
        return changePasswordData
    }


    fun likeUnlikePost(
        likeFavouriteRequest: PostDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.LIKE_POST
        val likeUnlikeData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(likeFavouriteRequest, apiName)
        apiServices?.likePost(
            likeFavouriteRequest.user_id,
            likeFavouriteRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val likeUnlikeResponse = response as BaseResponse
                printStrJson(likeUnlikeResponse, apiName)
                likeUnlikeData.value = likeUnlikeResponse
            }
        }))
        return likeUnlikeData
    }

    fun favUnFavPost(
        likeFavouriteRequest: PostDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.FAVORITE_POST
        val favUnFavData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(likeFavouriteRequest, apiName)
        apiServices?.favoritePost(
            likeFavouriteRequest.user_id, likeFavouriteRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val favUnFavPostResponse = response as BaseResponse
                printStrJson(favUnFavPostResponse, apiName)
                favUnFavData.value = favUnFavPostResponse
            }
        }))
        return favUnFavData
    }

    fun searchUser(
        searchUserRequest: SearchUserRequest,
        progressShow: Boolean,
    ): MutableLiveData<SearchUserData> {
        val apiName = APIServices.SEARCH_USER
        val searchUserData: MutableLiveData<SearchUserData> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(searchUserRequest, apiName)
        apiServices?.searchUser(
            searchUserRequest.user_id,
            searchUserRequest.text
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val searchUserResponse = response as SearchUserData
                printStrJson(searchUserResponse, apiName)
                searchUserData.value = searchUserResponse
            }
        }))
        return searchUserData
    }

    fun newPost(
        userIdBody: RequestBody,
        contentBody: RequestBody,
        multipartFiles: List<MultipartBody.Part>,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.NEW_POST
        val searchUserData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
//        printStrRequestJson(searchUserRequest, apiName)
        apiServices?.newPost(userIdBody,
            contentBody, multipartFiles
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val searchUserResponse = response as BaseResponse
                printStrJson(searchUserResponse, apiName)
                searchUserData.value = searchUserResponse
            }
        }))
        return searchUserData
    }

    fun postList(
        postListRequest: PostListRequest,
        progressShow: Boolean,
    ): MutableLiveData<PostResponse> {
        val apiName = APIServices.POST_LIST
        val postData: MutableLiveData<PostResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(postListRequest, apiName)
        apiServices?.postList(
            postListRequest.user_id,
            postListRequest.offset.toString(),
            postListRequest.limit.toString()
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val postResponse = response as PostResponse
                printStrJson(postResponse, apiName)
                postData.value = postResponse
            }
        }))
        return postData
    }

    private fun printStrRequestJson(request: Any, apiName: String) {
        try {
            val json = Gson().toJson(request)
            if (BuildConfig.DEBUG) {
                Log.d("API---Request---$apiName", json)
            }
        } catch (e: Exception) {
        }
    }

    private fun printStrJson(response: Any, apiName: String) {
        try {
            val json = Gson().toJson(response)
            if (BuildConfig.DEBUG) {
                Log.d("API---Response---$apiName", json)
            }
        } catch (e: Exception) {
        }
    }
}
