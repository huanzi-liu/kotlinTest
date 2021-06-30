package com.example.testdemo.lifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.testdemo.R
import com.example.testdemo.databinding.ActivityLifecycleBinding

class LifecycleActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLifecycleBinding
    private lateinit var model:LifecycleModel
    private lateinit var model2:LifecycleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_lifecycle)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lifecycle)
        model = ViewModelProvider(this).get(LifecycleModel::class.java)
        model2 = ViewModelProvider(this).get(LifecycleViewModel::class.java)
        init()
    }

    fun init() {
        model.result.observe(this){
            binding.tv.text = it
        }
        model.getStr()

        model2.datas.observe(this){
            binding.tv2.text = it
        }
        model2.getConfig()

    }

}