package com.example.testdemo.ashmem

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.testdemo.R
import com.example.testdemo.ashmem.AshmemService.Companion.BITMAP
import java.io.FileInputStream

class AshmemActivity : AppCompatActivity() {

    private var mService:IBinder?= null

    private var iv: ImageView? = null
    private var tv: TextView? = null
    private var tv2: TextView? = null

    private val service = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = service
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ashmem)
        iv = findViewById(R.id.iv)
        tv = findViewById(R.id.tv)
        tv2 = findViewById(R.id.tv2)

        tv?.setOnClickListener { bindService() }
        tv2?.setOnClickListener { getBitMap() }
    }

    private fun getBitMap() {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()

        try {
            mService?.transact(BITMAP,data,reply,0)
            Log.i("---","-------${reply.dataSize()}")
            val descriptor = reply.readFileDescriptor().fileDescriptor
            val fileInputStream = FileInputStream(descriptor)
            val bytes = fileInputStream.readBytes()
            if (bytes.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                bitmap?.let { iv?.setImageBitmap(bitmap) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            data.recycle()
            reply.recycle()
        }
    }


    private fun bindService() {
//        Intent().apply {
//            action = "com.example.ashmem.Server.Action"
//            setPackage("com.example.testdemo")
//        }.also {
//            intent -> bindService(intent,service, Context.BIND_AUTO_CREATE)
//        }
        Log.i("---","bindService")
        val intent = Intent()
        intent.action = "com.example.ashmem.Server.Action"
        intent.setPackage("com.example.testdemo")
        Log.i("---","band seccess ${bindService(intent,service,Context.BIND_AUTO_CREATE)}")
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(service)
    }
}