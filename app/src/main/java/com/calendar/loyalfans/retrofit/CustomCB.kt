package com.calendar.loyalfans.retrofit

import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.model.response.BaseResponse
import com.calendar.loyalfans.utils.Common
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.net.HttpURLConnection.*


class CustomCB<T>(isShowProgressDialog: Boolean, onAPIResponse: OnAPIResponse) : Callback<T> {
    private var onAPIResponse: OnAPIResponse? = null

    interface OnAPIResponse {
        fun onResponse(response: Any)
    }

    override fun onResponse(
        call: Call<T>,
        response: Response<T>,
    ) {
        dismissProgress()
        if (response.code() == HTTP_OK) {
            try {
                val isBaseResponse = response.body() is BaseResponse
                if (isBaseResponse) {
                    val baseResponse = response.body() as BaseResponse
                    if (!baseResponse.status) {
                        Common.showToast(BaseActivity.getActivity(), baseResponse.msg)
                    }
                }
            } catch (e: java.lang.Exception) {
            }
            val body = response.body()
            if (body != null) {
                onAPIResponse?.onResponse(body)
            }
        } else {
            try {
                val type: Type = object : TypeToken<BaseResponse?>() {}.type
                val errorResponse: BaseResponse? =
                    Gson().fromJson(response.errorBody()?.charStream(), type)
                if (errorResponse != null && errorResponse.msg.isNotEmpty()) {
                    Common.showToast(BaseActivity.getActivity(), errorResponse.msg)
                }

                if (response.code() == HTTP_UNAUTHORIZED || response.code() == HTTP_FORBIDDEN) {
                    Common.automaticallyLogoutOnUnauthorizedOrForbidden()
                } else if (errorResponse != null && response.code() == HTTP_NOT_FOUND && errorResponse.msg == "Unauthorized") {
                    Common.automaticallyLogoutOnUnauthorizedOrForbidden()
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
        dismissProgress()
        Common.showToast(BaseActivity.getActivity(), t.message.toString())
    }

    private fun dismissProgress() {
        Common.dismissProgress()
    }

    init {
        if (isShowProgressDialog) {
            Common.displayProgress(BaseActivity.getActivity())
        }
        this.onAPIResponse = onAPIResponse
    }
}