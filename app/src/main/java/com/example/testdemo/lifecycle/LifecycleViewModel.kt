package com.example.testdemo.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LifecycleViewModel : BaseViewModel() {

    val repository by lazy { LifecycleRep() }

    val data: MutableLiveData<String> = MutableLiveData()
    val datas :LiveData<String> get() = data
    val msg: MutableLiveData<String> = MutableLiveData()

    fun getConfig() = launchUi {
        var result = repository.getCf()
//        var result = repository.getSay()
        handelResponseResult(result, {
            data.value = result.data.toString()
        }, {
            data.value = result.msg
        })
    }

}