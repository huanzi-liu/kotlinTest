package com.example.testdemo.lifecycle

import androidx.lifecycle.*
import com.example.testdemo.http.Api
import com.example.testdemo.http.DataBean
import com.example.testdemo.http.RetrofitFactory
import kotlinx.coroutines.launch

class LifecycleModel : ViewModel() {
    var res: MutableLiveData<String> = MutableLiveData()
    val result: LiveData<String> get() = res

    fun getStr() {
        viewModelScope.launch {
            val dataAny = RetrofitFactory.createRetrofit().create(Api::class.java).getLeanConfig().apply {
                if (this.code == 0) {

                }
            }
            res.value = dataAny.data.toString()
        }
    }

}