package com.example.testdemo.coroutine

import com.example.testdemo.http.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("translate?doctype=json")
    suspend fun translate(@Field("i") i: String): ApiResult<Result>
}

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val errorCode: Int, val errorMsg: String) : ApiResult<Nothing>()
}

data class Result(val Type: String, val elapsedTime: Int, val translateResult: List<List<TranslateResult>>) {
    data class TranslateResult(val src: String, val tgt: String)
}

object Api{val service:ApiService by lazy { retrofit.create(ApiService::class.java) }}
private val okHttpClient = OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).build()

val retrofit = Retrofit.Builder().addCallAdapterFactory(ApiResultCallAdapterFactory()).addConverterFactory(GsonConverterFactory.create()).baseUrl("http://fanyi.youdao.com/").client(okHttpClient).build()

