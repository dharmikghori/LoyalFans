package com.calendar.loyalfans.retrofit

import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


open class RetrofitService {

    companion object {
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        private val retrofit = Retrofit.Builder()
            .baseUrl(APIServices.SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttp)
            .build()

        //Use this for SSL verification otherwise UnsafeOkHttpClient().getUnsafeOkHttpClient()
        private val okHttp: OkHttpClient
            get() {
                val okHttpBuilder = OkHttpClient.Builder()
                okHttpBuilder.readTimeout(5, TimeUnit.MINUTES)
                okHttpBuilder.writeTimeout(5, TimeUnit.MINUTES)
                okHttpBuilder.addInterceptor(getInterceptor())
                return okHttpBuilder.build()
            }

        fun getInterceptor(): Interceptor {
            return Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                val request = builder
                    .header("App-Secret-Key",
                        SPHelper(BaseActivity.getActivity()).getLoginAppSecretKey())
                    .header("Authorization", Credentials.basic("admin", "1234"))
                    .header("Authorization_token", APIServices.AUTH_TOKEN)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                chain.proceed(request)
            }
        }

        fun disabledSSL() {

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