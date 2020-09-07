package com.calendar.loyalfans.retrofit

import com.calendar.loyalfans.ui.BaseActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


open class RetrofitService {


    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl(APIServices.SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
        private val okHttp: OkHttpClient
            get() {
                val okHttpBuilder = OkHttpClient.Builder()
                okHttpBuilder.readTimeout(5, TimeUnit.MINUTES)
                okHttpBuilder.writeTimeout(5, TimeUnit.MINUTES)
                okHttpBuilder.addInterceptor(getInterceptor())
                return okHttpBuilder.build()
            }

        private fun getInterceptor(): Interceptor {

            return Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                val request = builder
                    .header("APP-SECRET-KEY",
                        SPHelper(BaseActivity.getActivity()).getLoginAppSecretKey())
                    .header("Authorization", Credentials.basic("admin", "1234"))
                    .header("AUTHORIZATION_TOKEN", APIServices.AUTH_TOKEN)
//                    .header("Accept", "*/*")
                    .header("Content-Type", "application/x-www-form-urlencoded")
//                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
        }

        fun <S> createService(serviceClass: Class<S>): S? {
            return if (Common.isOnline(BaseActivity.getActivity(), true)) {
                retrofit.create(serviceClass)
            } else {
                null
            }
        }
    }
}