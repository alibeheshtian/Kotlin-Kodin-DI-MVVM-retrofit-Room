package com.shenoto.domain.di

import com.shenoto.domain.BuildConfig
import com.shenoto.domain.services.ApiService
import com.shenoto.domain.services.HttpInterceptor
import com.shenoto.domain.services.PrefsUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val MODULE_NAME = "Network Module"
val networkModule = Module(MODULE_NAME, false) {
    bind<OkHttpClient>() with singleton { getOkHttpClient(instance()) }
    bind<Retrofit>() with singleton { getRetrofit(instance()) }
    bind<ApiService>() with singleton { getApiService(instance()) }
}


private fun getOkHttpClient(prefsUtils: PrefsUtils): OkHttpClient {
    val httpBuilder = OkHttpClient.Builder()
    httpBuilder.addInterceptor(HttpInterceptor(prefsUtils)).connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).interceptors().apply {
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                this.add(httpLoggingInterceptor)
            }
        }
    return httpBuilder.build()
}

private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient).build()

private fun getApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

