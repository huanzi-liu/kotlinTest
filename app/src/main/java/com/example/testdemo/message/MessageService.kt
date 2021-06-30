package com.example.testdemo.message

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class MessageService :Service() {

    companion object{
        const val SAY_HELLO = 1
        const val FROM_SERVICE = 2
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Messenger(TestHandler()).binder
    }

    internal class TestHandler:Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SAY_HELLO->{
                    replyToClient(msg,"==--33--==")
                }
            }
        }

        private fun replyToClient(msg:Message,replyText:String) {
            val client = msg.replyTo
            val reply = Message.obtain(null , FROM_SERVICE)
            reply.data = Bundle().apply {
                putString("reply",replyText)
            }
            try {
                client?.send(reply)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}