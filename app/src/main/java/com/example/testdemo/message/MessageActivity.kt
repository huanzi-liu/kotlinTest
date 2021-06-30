package com.example.testdemo.message

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.example.testdemo.R
import com.example.testdemo.message.MessageService.Companion.FROM_SERVICE
import com.example.testdemo.message.MessageService.Companion.SAY_HELLO
import java.io.Serializable
import java.io.SerializablePermission
import java.time.Instant

class MessageActivity : AppCompatActivity() {

    private var bind: AppCompatTextView? = null
    private var say: AppCompatTextView? = null

    private var messenger: Messenger? = null
    private var cMessenger = Messenger(CHandler())

    private var service = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            messenger = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        init()
    }

    private fun init() {
        bind = findViewById(R.id.bind)
        say = findViewById(R.id.say)

        bind?.setOnClickListener { bind() }
        say?.setOnClickListener { say() }
    }

    private fun bind() {
        Intent().apply {
            action = "com.example.msg.Server.Action"
            setPackage("com.example.testdemo")
        }.also {intent-> bindService(intent, service, Context.BIND_AUTO_CREATE)
        }
    }

    private fun say() {
        val msg = Message.obtain(null,SAY_HELLO,0,0)
        msg.replyTo = cMessenger
        msg.data = Bundle().apply {
//            putSerializable("pppp",SerializablePermission("0-0"))
            putString("ppp","sssssss")
        }
        try {
            messenger?.send(msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class CHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                FROM_SERVICE->{
                    Log.i("---","message ${msg?.data?.getString("reply")}")
                }
                else-> super.handleMessage(msg)
            }
        }

    }
}