package com.shenoto.domain.services


import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.shenoto.domain.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


class HttpInterceptor(private val prefsUtils: PrefsUtils) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()

        builder.header("Content-Type", "application/json")
        builder.header("Accept", "application/json")

        if (prefsUtils.getAccessesToken() != null) {
            builder.header("Authorization", String.format("Bearer %s", prefsUtils.getAccessesToken()))
        }

        request = builder.build() //overwrite old request
        val response = chain.proceed(request) //perform request, here original request will be executed

//
//        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED && request.url().toString() != "${BuildConfig.BASE_URL_API}auth/login") {
//            synchronized(this) {
//                //perform all 401 in sync blocks, to avoid multiply token updates
//
//                val code = refreshToken() / 100 //refresh token
//                if (code != 2) { //if refresh token failed for some reason
//                    if (code == 4)
//                    //only if response is 400, 500 might mean that token was not updated
//                        prefsUtils.logout() //go to login screen
//                    return response //if token refresh failed - show error to user
//                }
//
//                builder.header("Authorization", String.format("Bearer %s", prefsUtils.getAccessesToken()))
//
//
//                request = builder.build()
//                return chain.proceed(request)
//
//            }
//        }



        if (response.code() / 100 == 5) {
            ToastUtils.showLong("اشکال از سمت سرور")
        }
        return response
    }


//    private fun refreshToken(): Int {
//        var code = HttpURLConnection.HTTP_UNAUTHORIZED
//        val okHttpClient =
//            OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS).build()
//        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_API)
//            .addConverterFactory(GsonConverterFactory.create(Gson())).client(okHttpClient).build()
//        val parameter = mutableMapOf<String, String>()
//
//        parameter["refresh_token"] = prefsUtils.getRefreshToken()!!
//
//        val responseObservable = retrofit.create(ApiService::class.java).refreshToken(parameter)
//        val execute = responseObservable.execute()
//        execute.body()?.let {
//            prefsUtils.setRefreshToken(it.refreshToken)
//            prefsUtils.setAccessesToken(it.accessToken)
//            code = HttpURLConnection.HTTP_OK
//        }
//        return code
//    }
}
