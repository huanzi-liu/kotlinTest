package com.example.testdemo.http

import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("leanConfig/common/config")
    suspend fun getLeanConfig():DataBean<Any?>

    @GET("leanConfig/common/config")
    suspend fun getLeanConfig2():Response<DataBean<*>>
}