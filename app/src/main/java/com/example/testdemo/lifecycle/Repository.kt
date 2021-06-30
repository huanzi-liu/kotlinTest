package com.example.testdemo.lifecycle

import com.example.testdemo.http.DataBean
import com.google.gson.JsonParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.text.ParseException

open class Repository {
    companion object {
        const val ERROR_NOTWORK = 1
        const val ERROR_NOTWORK_POOR = 2
        const val ERROR_PARSE = 3
        const val ERROR_UNKNOWN = 4

        const val ERROR_NOTWORK_MSG = "网络错误"
        const val ERROR_NOTWORK_POOR_MSG = "网络不稳定"
        const val ERROR_PARSE_MSG = "解析错误"
        const val ERROR_UNKNOWN_MSG = "未知错误"
    }

    suspend fun <T> apiCall(call: suspend () -> DataBean<T>): DataBean<T> {
        return withContext(Dispatchers.IO) {
            try {
                call.invoke()
            } catch (e: Exception) {
                handleCallException(e)
            }
        }
    }

    private fun <T> handleCallException(exception: Exception): DataBean<T> {
        return when (exception) {
            is JSONException, is JsonParseException, is ParseException -> {
                DataBean(ERROR_PARSE, ERROR_PARSE_MSG, exception.printStackTrace() as T)
            }
            is ConnectException, is UnknownHostException -> {
                DataBean(ERROR_NOTWORK, ERROR_NOTWORK_MSG, exception.printStackTrace() as T)
            }
            is SocketException -> {
                DataBean(ERROR_NOTWORK_POOR, ERROR_NOTWORK_POOR_MSG, exception.printStackTrace() as T)
            }
            else -> {
                DataBean(ERROR_UNKNOWN, ERROR_UNKNOWN_MSG, exception.printStackTrace() as T)
            }
        }
    }
}