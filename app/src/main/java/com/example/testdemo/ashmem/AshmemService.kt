package com.example.testdemo.ashmem

import android.app.Application
import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import com.example.testdemo.R
import com.example.testdemo.base.BaseApplication
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor

class AshmemService : Service() {

    companion object{
    const val BITMAP = 2
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("---","onBind")
        return AshmenBinder()
    }

    class AshmenBinder : Binder() {
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean = when(code) {
            BITMAP->{
                try {
                    val bitmap = createBitmap()
                    Log.i("---","bitmap="+bitmap)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                    val bytes = stream.toByteArray()
                    writeData(bytes,reply)
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
            else ->  super.onTransact(code, data, reply, flags)
        }

        private fun writeData(contentBytes:ByteArray,reply: Parcel?) {
            val memoryFile = MemoryFile("mFile", contentBytes.size)
            memoryFile.writeBytes(contentBytes,0,0,contentBytes.size)
            val method = MemoryFile::class.java.getDeclaredMethod("getFileDescriptor")
            val descriptor = method.invoke(memoryFile) as? FileDescriptor
            val parcel = ParcelFileDescriptor.dup(descriptor)
            parcel.fileDescriptor.let { reply?.writeFileDescriptor(it) }

        }

        private fun createBitmap(): Bitmap {
            val res = BitmapFactory.decodeResource(BaseApplication.application.resources, R.mipmap.ic_launcher)
            return Bitmap.createScaledBitmap(res, 1024, 1024, false)
        }
    }

}