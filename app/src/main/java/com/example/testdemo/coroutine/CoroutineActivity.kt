package com.example.testdemo.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testdemo.R
import com.example.testdemo.coroutine.ui.main.CoroutineFragment

class CoroutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coroutine_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CoroutineFragment.newInstance())
                    .commitNow()
        }
    }
}