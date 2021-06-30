package com.example.testdemo.algorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testdemo.R

class AlgorithmActivity : AppCompatActivity() {
    val TAG = "algorithm"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algorithm)
        init()
    }

    fun init() {
        Log.i("algorithm","${Solution().calculate("1+2-3*4/((5+6-7)*8)/9")}")
        Log.i(TAG,"${Solution().searchLeft(intArrayOf(1,2,5,6,9),5)}")
        Log.i(TAG,"${Solution().searchRight(intArrayOf(1,2,5,6,9),5)}")
        Log.i(TAG,"${Solution().searchRight(intArrayOf(1,2,3,5,6,9),4)}")
        Log.i(TAG,"${Solution().searchLeft(intArrayOf(1,2,3,5,6,9),4)}")
    }
}