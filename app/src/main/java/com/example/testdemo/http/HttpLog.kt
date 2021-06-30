package com.example.testdemo.http

import android.util.Log
import com.example.testdemo.utils.AppUtils
import okhttp3.logging.HttpLoggingInterceptor

class HttpLog: HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        if (AppUtils.isJson(message)) {
            Log.i("HttpLog","is json $message")
        }else{
            Log.i("HttpLog","not json $message")
        }
    }
}