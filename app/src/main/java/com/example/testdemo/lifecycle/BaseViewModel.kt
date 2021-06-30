package com.example.testdemo.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdemo.http.DataBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    fun launchUi(launchBlock: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            coroutineScope {
                launchBlock()
            }
        }
    }

    suspend fun handelResponseResult(response: DataBean<Any>,
                                     successBlock: suspend CoroutineScope.() -> Unit,
                                     failureBlock: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            if (response.code == 0) {
                successBlock()
            } else {
                failureBlock()
            }
        }

    }

    suspend fun handelResponseResult(response: DataBean<Any>,
                                     successBlock: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            if (response.code == 0) {
                successBlock()
            }
        }

    }
}