package com.example.testdemo.lifecycle

import com.example.testdemo.http.DataBean
import com.example.testdemo.http.SayingBean
import com.example.testdemo.http.UserConfigBean
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface LifecycleApi {
    @GET("leanConfig/common/config")
    fun getConfig():Deferred<DataBean<UserConfigBean>>

//    @GET("leanConfig/common/config")
//    suspend fun getConfig():DataBean<UserConfigBean>

    @GET("common/getSaying")
    suspend fun getSaying(): DataBean<SayingBean>
}