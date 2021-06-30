package com.example.testdemo.lifecycle

import android.util.Log
import com.example.testdemo.http.DataBean
import com.example.testdemo.http.SayingBean
import com.example.testdemo.http.UserConfigBean
import kotlin.math.log

class LifecycleRep : Repository() {
    suspend fun getCf(): DataBean<UserConfigBean> {
        return apiCall { RetrofitFactory.createRetrofit()?.getConfig()?.await()!!}.apply {
            Log.i("ssss",this.toString())
        }
    }
//    suspend fun getCf(): DataBean<Any> {
//        return apiCall { RetrofitFactory.createRetrofit()?.getConfig() as DataBean<Any>}.apply {
//            Log.i("ssss",this.toString())
//        }
//    }
    suspend fun getSay(): DataBean<SayingBean> {
        return apiCall { RetrofitFactory.createRetrofit()?.getSaying() as DataBean<SayingBean>}.apply {
            Log.i("ssss",this.toString())
        }
    }
}