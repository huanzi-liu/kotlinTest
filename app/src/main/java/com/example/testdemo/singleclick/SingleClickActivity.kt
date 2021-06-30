package com.example.testdemo.singleclick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.testdemo.R
import com.example.testdemo.utils.getValue
import com.example.testdemo.utils.showShortToast

class SingleClickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_click)
        init()
    }

    private fun init() {
        val tv = findViewById<AppCompatTextView>(R.id.tv)
        val tv2 = findViewById<AppCompatTextView>(R.id.tv2)
        tv.onSingleClick(interval = 10000, isShareSingleClick = false, listener = View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("s","sssss")
            intent.putExtra("s","eeee")
            val value by lazy {
                getValue("s","3")
            }
            Log.i("tag", "ssssss")
            showShortToast("$value")
        })
        tv2.onSingleClick(1000,true,listener = View.OnClickListener {  Log.i("tag", "aaaaaa") } )
    }
}