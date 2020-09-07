package com.gama.theearningapp.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calendar.loyalfans.BuildConfig
import com.calendar.loyalfans.model.request.ForgotPasswordRequest
import com.calendar.loyalfans.model.request.LoginRequest
import com.calendar.loyalfans.model.request.RegistrationRequest
import com.calendar.loyalfans.model.response.BaseResponse
import com.calendar.loyalfans.model.response.LoginResponse
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.retrofit.CustomCB
import com.calendar.loyalfans.retrofit.RetrofitService
import com.google.gson.Gson


class BaseViewModel : ViewModel() {

    fun login(
        loginRequestData: LoginRequest,
        progressShow: Boolean,
    ): MutableLiveData<LoginResponse> {
        val loginResponseData: MutableLiveData<LoginResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(loginRequestData, APIServices.SIGN_IN)
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
                printStrJson(loginResponse, APIServices.SIGN_IN)
                loginResponseData.value = loginResponse
            }
        }))
        return loginResponseData
    }


    fun registerNewUser(
        registrationRequest: RegistrationRequest,
        progressShow: Boolean,
    ): MutableLiveData<LoginResponse> {
        val loginResponseData: MutableLiveData<LoginResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(registrationRequest, APIServices.SIGN_UP)
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
                printStrJson(loginResponse, APIServices.SIGN_UP)
                loginResponseData.value = loginResponse
            }
        }))
        return loginResponseData
    }

    fun forgotPassword(
        forgotPasswordRequest: ForgotPasswordRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val forgotPasswordData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(forgotPasswordRequest, APIServices.FORGOT_PASSWORD)
        apiServices?.forgotPassword(
            forgotPasswordRequest.email
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val forgotPasswordResponse = response as BaseResponse
                printStrJson(forgotPasswordResponse, APIServices.FORGOT_PASSWORD)
                forgotPasswordData.value = forgotPasswordResponse
            }
        }))
        return forgotPasswordData
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
