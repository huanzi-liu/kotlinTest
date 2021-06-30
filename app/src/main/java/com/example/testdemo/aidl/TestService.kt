package com.example.testdemo.aidl

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import java.util.concurrent.CopyOnWriteArrayList

class TestService : Service() {

    private val listBean = CopyOnWriteArrayList<TestBean?>()

    private val mBinder: Binder = object : Test.Stub() {
        override fun testAidl(aInt: Int) {

        }

        override fun getBean(): MutableList<TestBean?> = listBean

        override fun addTestBean(bean: TestBean?) {
            listBean.add(bean)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        val check = checkCallingOrSelfPermission("com.example.testdemo.aidl.ACCESS_TEST_SERVER")
        if (check == PackageManager.PERMISSION_DENIED) {
            return null
        }
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        listBean.add(TestBean("test"))
    }
}