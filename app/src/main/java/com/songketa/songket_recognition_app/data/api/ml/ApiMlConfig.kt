package com.songketa.songket_recognition_app.data.api.ml

import com.songketa.songket_recognition_app.BuildConfig
import com.songketa.songket_recognition_app.data.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiMlConfig {
    fun getApiMlService(): ApiService {
        val loggingInterceptor = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }else {
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("x-api-key", BuildConfig.API_KEY)
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_POINT_ML)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}