package com.example.testdemo.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewModelScope
import com.example.testdemo.R
import com.example.testdemo.http.DataSource
import kotlinx.coroutines.CoroutineScope

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        val layout = findViewById<ConstraintLayout>(R.id.layout)
//        layout.loadLayoutDescription(R.xml.state)
//        layout.setOnClickListener { layout.setState(R.id.end,0,0) }
        getData()

    }


     fun getData() {
        DataSource().enqueueLoading({
            getLeanConfig()
        }){
            onSuccess{
                Log.i("aaa","$it")
            }
            onFailed{
                Log.i("eee","${it.exception.toString()}")
            }
        }
    }
}