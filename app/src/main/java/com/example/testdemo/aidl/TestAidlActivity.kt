package com.example.testdemo.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.example.testdemo.R
import com.example.testdemo.aidl.Test

class TestAidlActivity : AppCompatActivity() {

    private var server: Test? = null
    private var iBinder: IBinder? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iBinder = service
            service?.linkToDeath(deathRecipient, 0)
            server = Test.Stub.asInterface(service)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_aidl)
        init()
    }

    private fun init() {
        findViewById<AppCompatTextView>(R.id.test).setOnClickListener { connectService() }
        findViewById<AppCompatTextView>(R.id.add).setOnClickListener { addTestBean() }
        findViewById<AppCompatTextView>(R.id.get).setOnClickListener { getBean() }
    }

    private fun connectService() {
        val intent = Intent()
        intent.action = "com.example.aidl.Server.Action"
        intent.setPackage("com.example.testdemo")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun addTestBean() {
        server?.addTestBean(TestBean("activity"))
    }

    private fun getBean() {
        val list = server?.bean
        list?.let {
            Log.i("sss", "list ${it.toString()}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    private val deathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            iBinder?.unlinkToDeath(this, 0)
        }
    }

}