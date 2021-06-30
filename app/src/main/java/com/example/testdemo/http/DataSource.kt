package com.example.testdemo.http

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

class DataSource() : BaseDateSource<Api>(Api::class.java) {

}
abstract class BaseDateSource<Api : Any>(val apiService: Class<Api>) : CoroutineEvent {

    fun getApi(): Api {
        return getApi(apiService)
    }

    fun getApi(apiService: Class<Api>): Api {
        val retrofit = RetrofitFactory.createRetrofit()
        return retrofit.create(apiService)
    }

    fun handleException(throwable: Throwable, callback: BaseRequestCallback?) {
        if (null == callback) {
            return
        }
        if (throwable is CancellationException) {
            callback.onCancelled?.invoke()
            return
        }
        val exception = generateBaseExceptionReal(throwable)
        if (exceptionHandle(exception)) {
            callback.onFailed?.invoke(exception)
            if (callback.onFailToast()) {
                val error = exceptionFormat(exception)
                if (error.isNotBlank()) {
                    Log.i("error", error)
                }
            }

        }

    }

    fun generateBaseExceptionReal(throwable: Throwable): BaseHttpException {
        return if (throwable is BaseHttpException) {
            throwable
        } else {
            LocalException(throwable)
        }
    }

    fun exceptionHandle(exception: BaseHttpException): Boolean {
        return true
    }

    open fun exceptionFormat(exception: BaseHttpException): String {
        return when (exception.exception) {
            null -> {
                exception.msg
            }
            is ConnectException,
            is SocketException,
            is UnknownHostException -> {
                "超时"
            }
            else -> {
                "异常:${exception.msg}"
            }
        }
    }

    fun <Data> enqueueLoading(apiFun: suspend Api.() -> DataBean<Data>, callbackFun: (RequestCallBack<Data>.() -> Unit)? = null): Job {
        return enqueue(apiFun = apiFun, showLoading = true, callbackFun = callbackFun)
    }

    fun <Data> enqueue(apiFun: suspend Api.() -> DataBean<Data>, showLoading: Boolean = false, callbackFun: (RequestCallBack<Data>.() -> Unit)? = null): Job {
        return launchMain {
            val callBack = if (callbackFun == null) null else RequestCallBack<Data>().apply { callbackFun.invoke(this) }
            if (showLoading) {
                Log.i("showLoading", "${coroutineContext[Job]}")
            }

            try {
                callBack?.onStart?.invoke()
                var data: DataBean<Data>
                try {
                    data = apiFun.invoke(getApi())
                } catch (e: Throwable) {
                    handleException(e, callBack)
                    return@launchMain
                }
                getResponse(callBack, data as Data)
            } finally {
                try {
                    callBack?.onFinally?.invoke()
                } finally {
                    if (showLoading) {
                        Log.i("showLoading", "dismiss")
                    }
                }
            }

        }
    }


    suspend fun <Data> getResponse(callBack: RequestCallBack<Data>?, data: Data) {
        callBack?.let {
            withNonCancellable {
                callBack.onSuccess?.let {
                    withMain { it.invoke(data) }
                }
                callBack.onSuccessIO?.let {
                    withIo { it.invoke(data) }
                }
            }
        }
    }

    override val coroutineScope: CoroutineScope = GlobalScope

}

open class BaseRequestCallback(internal var onStart: (() -> Unit)? = null,
                               internal var onCancelled: (() -> Unit)? = null,
                               internal var onFailed: ((BaseHttpException) -> Unit)? = null,
                               internal var onFailToast: (() -> Boolean) = { true },
                               internal var onFinally: (() -> Unit)? = null, ) {
    fun onStart(block: () -> Unit) {
        onStart = block
    }

    fun onCancelled(block: () -> Unit) {
        onCancelled = block
    }

    fun onFailed(block: (BaseHttpException) -> Unit) {
        onFailed = block
    }

    fun onFailToast(block: () -> Boolean) {
        onFailToast = block
    }

    fun onFinally(block: () -> Unit) {
        onFinally = block
    }

}

class RequestCallBack<Data>(internal var onSuccess: ((Data) -> Unit)? = null,
                            internal var onSuccessIO: (suspend (Data) -> Unit)? = null) : BaseRequestCallback() {

    fun onSuccess(block: (Data) -> Unit) {
        onSuccess = block
    }

    fun onSuccessIO(block: suspend (Data) -> Unit) {
        onSuccessIO = block
    }
}