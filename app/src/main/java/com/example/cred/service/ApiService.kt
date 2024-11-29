package com.example.cred.service

import com.example.cred.model.ApiResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


interface MyApiService {
    @GET("p6764/test_mint")
    suspend fun fetchData(): ApiResponse
}

object RetrofitInstance {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
        .build()
    val api: MyApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.mocklets.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApiService::class.java)
    }
}