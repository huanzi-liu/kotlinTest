package com.example.testdemo.coroutine.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdemo.coroutine.Api
import com.example.testdemo.coroutine.ApiResult
import com.example.testdemo.coroutine.ApiService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var _result: MutableLiveData<String> = MutableLiveData()
    val result: LiveData<String>
        get() = _result

    fun success(src: String) {
        viewModelScope.launch {
            when (val res = Api.service.translate(src)) {
                is ApiResult.Success -> {
                    _result.value = res.data.translateResult[0][0].tgt
                }
                is ApiResult.Failure -> {
                    _result.value = "errorCode ${res.errorCode} errorMsg ${res.errorMsg}"
                }
            }
        }
    }

}