package com.example.testdemo.coroutine

import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.math.log


class ApiResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        check(getRawType(returnType) == Call::class.java) { "$returnType must be retrofit2.Call" }
        check(returnType is ParameterizedType) { "$returnType must be parameterized,Rew types are not supported" }

        val apiResultType = getParameterUpperBound(0, returnType)
        check(getRawType(apiResultType) == ApiResult::class.java) { "$apiResultType must be apiResult" }
        check(apiResultType is ParameterizedType) { "$returnType must be parameterized,Rew types are not supported" }

        return ApiResultCallAdapter<Any>(getParameterUpperBound(0, apiResultType))
    }

}

class ApiResultCallAdapter<T>(private val type: Type) : CallAdapter<T, Call<ApiResult<T>>> {
    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<ApiResult<T>> {
        return ApiResultCall(call)
    }

}

class ApiResultCall<T>(private val delegate: Call<T>) : Call<ApiResult<T>> {
    override fun clone(): Call<ApiResult<T>> {
        return ApiResultCall(delegate.clone())
    }

    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException("ApiResultCall does not support synchronous execution")
    }

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val apiResult = if (response.body() == null) {
                        ApiResult.Failure(ApiError.dataIsNull.code, ApiError.dataIsNull.msg)
                    } else {
                        ApiResult.Success(response.body()!!)
                    }
                    callback.onResponse(this@ApiResultCall, Response.success(apiResult))
                } else {
                    callback.onResponse(this@ApiResultCall, Response.success(ApiResult.Failure(ApiError.httpStatesCodeError.code, ApiError.httpStatesCodeError.msg)))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val failureResult = if (t is ApiException) {
                    ApiResult.Failure(t.code, t.msg)
                } else {
                    ApiResult.Failure(ApiError.unknownError.code, ApiError.unknownError.msg)
                }
                callback.onResponse(this@ApiResultCall, Response.success(failureResult))
            }

        })
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

}
